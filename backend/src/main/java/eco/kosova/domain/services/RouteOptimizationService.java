package eco.kosova.domain.services;

import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.KontenierRepository;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Domain Service për optimizimin e rrugëve të mbledhjes së mbeturinave.
 * 
 * Ky service përdor algoritme të ndryshme për të gjetur rrugën
 * më të mirë për mbledhjen e kontejnerëve.
 * 
 * Strategjitë e disponueshme:
 * - NEAREST_NEIGHBOR: Fqinji më i afërt (greedy algorithm)
 * - PRIORITY_BASED: Bazuar në prioritetin e mbushjes
 * - ZONE_BASED: Mbledhje sipas zonave
 */
public class RouteOptimizationService {
    
    private static final Logger logger = Logger.getLogger(RouteOptimizationService.class.getName());
    
    private final KontenierRepository kontenierRepository;
    
    public RouteOptimizationService(KontenierRepository kontenierRepository) {
        this.kontenierRepository = kontenierRepository;
    }
    
    /**
     * Llogarit rrugën optimale për mbledhjen e kontejnerëve në një zonë.
     * Përdor algoritmin Nearest Neighbor.
     * 
     * @param zoneId ID-ja e zonës
     * @param startPoint Pika e fillimit (p.sh., vendndodhja e kamionit)
     * @return Lista e kontejnerëve të renditur sipas rrugës optimale
     */
    public List<Kontenier> calculateOptimalRoute(String zoneId, Coordinates startPoint) {
        logger.info(String.format("Calculating optimal route for zone %s", zoneId));
        
        // Merr kontejnerët që duhen mbledhur në zonë
        List<Kontenier> containersToCollect = kontenierRepository
            .findByZoneId(zoneId).stream()
            .filter(Kontenier::isReadyForCollection)
            .collect(Collectors.toList());
        
        if (containersToCollect.isEmpty()) {
            logger.info("No containers need collection in zone " + zoneId);
            return Collections.emptyList();
        }
        
        // Apliko algoritmin Nearest Neighbor
        return nearestNeighborRoute(containersToCollect, startPoint);
    }
    
    /**
     * Llogarit rrugën optimale duke përdorur prioritetet (urgent containers first).
     * 
     * @param zoneId ID-ja e zonës
     * @return Lista e kontejnerëve të renditur sipas prioritetit
     */
    public List<Kontenier> calculatePriorityBasedRoute(String zoneId) {
        logger.info(String.format("Calculating priority-based route for zone %s", zoneId));
        
        List<Kontenier> containers = kontenierRepository
            .findByZoneId(zoneId).stream()
            .filter(Kontenier::isReadyForCollection)
            .sorted(Comparator.comparingInt(c -> -c.getFillLevel().getValue())) // Descending
            .collect(Collectors.toList());
        
        logger.info(String.format("Route contains %d containers", containers.size()));
        
        return containers;
    }
    
    /**
     * Algoritmi Nearest Neighbor për route optimization.
     * Time complexity: O(n²)
     * 
     * @param containers Lista e kontejnerëve
     * @param startPoint Pika e fillimit
     * @return Lista e renditur
     */
    private List<Kontenier> nearestNeighborRoute(
            List<Kontenier> containers,
            Coordinates startPoint
    ) {
        List<Kontenier> route = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Coordinates currentPosition = startPoint;
        
        while (visited.size() < containers.size()) {
            // Gjen kontejnerin më të afërt që nuk është vizituar
            Kontenier nearest = null;
            double minDistance = Double.MAX_VALUE;
            
            for (Kontenier container : containers) {
                if (visited.contains(container.getId())) {
                    continue;
                }
                
                double distance = currentPosition.distanceTo(container.getLocation());
                
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = container;
                }
            }
            
            if (nearest != null) {
                route.add(nearest);
                visited.add(nearest.getId());
                currentPosition = nearest.getLocation();
            }
        }
        
        return route;
    }
    
    /**
     * Llogarit distancën totale të një rruge.
     * 
     * @param route Lista e kontejnerëve në rrugë
     * @param startPoint Pika e fillimit
     * @return Distanca totale në metra
     */
    public double calculateTotalDistance(List<Kontenier> route, Coordinates startPoint) {
        if (route.isEmpty()) {
            return 0.0;
        }
        
        double totalDistance = 0.0;
        Coordinates current = startPoint;
        
        for (Kontenier container : route) {
            totalDistance += current.distanceTo(container.getLocation());
            current = container.getLocation();
        }
        
        return totalDistance;
    }
    
    /**
     * Llogarit kohën e parashikuar për një rrugë.
     * 
     * @param route Lista e kontejnerëve
     * @param startPoint Pika e fillimit
     * @param averageSpeedKmh Shpejtësia mesatare në km/h
     * @param stopTimeMinutes Koha për të ndaluar në çdo kontejner (minuta)
     * @return Koha totale në minuta
     */
    public double estimateRouteTime(
            List<Kontenier> route,
            Coordinates startPoint,
            double averageSpeedKmh,
            double stopTimeMinutes
    ) {
        double distanceMeters = calculateTotalDistance(route, startPoint);
        double distanceKm = distanceMeters / 1000.0;
        
        double drivingTimeHours = distanceKm / averageSpeedKmh;
        double drivingTimeMinutes = drivingTimeHours * 60;
        
        double totalStopTime = route.size() * stopTimeMinutes;
        
        return drivingTimeMinutes + totalStopTime;
    }
    
    /**
     * Kthen informacion të detajuar për një rrugë.
     * 
     * @param route Lista e kontejnerëve
     * @param startPoint Pika e fillimit
     * @return RouteInfo object
     */
    public RouteInfo getRouteInfo(List<Kontenier> route, Coordinates startPoint) {
        double distance = calculateTotalDistance(route, startPoint);
        double estimatedTime = estimateRouteTime(route, startPoint, 30.0, 5.0);
        
        int totalCapacity = route.stream()
            .mapToInt(Kontenier::getCapacity)
            .sum();
        
        return new RouteInfo(
            route.size(),
            distance,
            estimatedTime,
            totalCapacity,
            route
        );
    }
    
    /**
     * Klasa e brendshme për informacion të rrugës
     */
    public static class RouteInfo {
        private final int containerCount;
        private final double totalDistanceMeters;
        private final double estimatedTimeMinutes;
        private final int totalCapacityLiters;
        private final List<Kontenier> containers;
        
        public RouteInfo(
                int containerCount,
                double totalDistanceMeters,
                double estimatedTimeMinutes,
                int totalCapacityLiters,
                List<Kontenier> containers
        ) {
            this.containerCount = containerCount;
            this.totalDistanceMeters = totalDistanceMeters;
            this.estimatedTimeMinutes = estimatedTimeMinutes;
            this.totalCapacityLiters = totalCapacityLiters;
            this.containers = Collections.unmodifiableList(containers);
        }
        
        // Getters
        public int getContainerCount() { return containerCount; }
        public double getTotalDistanceMeters() { return totalDistanceMeters; }
        public double getTotalDistanceKm() { return totalDistanceMeters / 1000.0; }
        public double getEstimatedTimeMinutes() { return estimatedTimeMinutes; }
        public double getEstimatedTimeHours() { return estimatedTimeMinutes / 60.0; }
        public int getTotalCapacityLiters() { return totalCapacityLiters; }
        public List<Kontenier> getContainers() { return containers; }
    }
}