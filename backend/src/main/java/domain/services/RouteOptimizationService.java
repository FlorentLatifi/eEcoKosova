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
 * Ky service përdor Strategy Pattern për të lejuar zgjedhjen e algoritmit
 * në runtime. Strategjitë e disponueshme:
 * - NEAREST_NEIGHBOR: Fqinji më i afërt (greedy algorithm)
 * - PRIORITY_BASED: Bazuar në prioritetin e mbushjes
 */
public class RouteOptimizationService {
    
    private static final Logger logger = Logger.getLogger(RouteOptimizationService.class.getName());
    
    private final KontenierRepository kontenierRepository;
    private final Map<String, RouteStrategy> strategies;
    
    public RouteOptimizationService(KontenierRepository kontenierRepository) {
        this.kontenierRepository = kontenierRepository;
        this.strategies = new HashMap<>();
        // Inicializo strategjitë e disponueshme
        this.strategies.put("NEAREST_NEIGHBOR", new NearestNeighborStrategy());
        this.strategies.put("PRIORITY_BASED", new PriorityBasedStrategy());
    }
    
    /**
     * Llogarit rrugën optimale për mbledhjen e kontejnerëve në një zonë.
     * Përdor strategjinë e specifikuar ose NEAREST_NEIGHBOR si default.
     * 
     * @param zoneId ID-ja e zonës
     * @param startPoint Pika e fillimit (p.sh., vendndodhja e kamionit)
     * @param strategyName Emri i strategjisë (NEAREST_NEIGHBOR, PRIORITY_BASED)
     * @return Lista e kontejnerëve të renditur sipas rrugës optimale
     */
    public List<Kontenier> calculateOptimalRoute(String zoneId, Coordinates startPoint, String strategyName) {
        logger.info(String.format("Calculating route for zone %s using strategy: %s", zoneId, strategyName));
        
        // Merr kontejnerët që duhen mbledhur në zonë
        List<Kontenier> containersToCollect = kontenierRepository
            .findByZoneId(zoneId).stream()
            .filter(Kontenier::isReadyForCollection)
            .collect(Collectors.toList());
        
        if (containersToCollect.isEmpty()) {
            logger.info("No containers need collection in zone " + zoneId);
            return Collections.emptyList();
        }
        
        // Zgjedh strategjinë
        RouteStrategy strategy = strategies.getOrDefault(
            strategyName != null ? strategyName.toUpperCase() : "NEAREST_NEIGHBOR",
            strategies.get("NEAREST_NEIGHBOR")
        );
        
        // Apliko strategjinë
        return strategy.calculateRoute(containersToCollect, startPoint);
    }
    
    /**
     * Llogarit rrugën optimale për mbledhjen e kontejnerëve në një zonë.
     * Përdor NEAREST_NEIGHBOR si strategji default (backward compatibility).
     * 
     * @param zoneId ID-ja e zonës
     * @param startPoint Pika e fillimit
     * @return Lista e kontejnerëve të renditur sipas rrugës optimale
     */
    public List<Kontenier> calculateOptimalRoute(String zoneId, Coordinates startPoint) {
        return calculateOptimalRoute(zoneId, startPoint, "NEAREST_NEIGHBOR");
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
            .collect(Collectors.toList());
        
        if (containers.isEmpty()) {
            logger.info("No containers need collection in zone " + zoneId);
            return Collections.emptyList();
        }
        
        // Përdor PriorityBasedStrategy
        RouteStrategy strategy = strategies.get("PRIORITY_BASED");
        Coordinates dummyStartPoint = new Coordinates(42.6629, 21.1655); // Default Prishtina
        return strategy.calculateRoute(containers, dummyStartPoint);
    }
    
    /**
     * Shton një strategji të re në runtime (për extensibility).
     * 
     * @param strategyName Emri i strategjisë
     * @param strategy Strategjia
     */
    public void addStrategy(String strategyName, RouteStrategy strategy) {
        logger.info(String.format("Adding new route strategy: %s", strategyName));
        strategies.put(strategyName.toUpperCase(), strategy);
    }
    
    /**
     * Kthen listën e strategjive të disponueshme.
     * 
     * @return Set i emrave të strategjive
     */
    public Set<String> getAvailableStrategies() {
        return new HashSet<>(strategies.keySet());
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