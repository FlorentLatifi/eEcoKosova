package eco.kosova.domain.services;

import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.valueobjects.Coordinates;

import java.util.*;
import java.util.logging.Logger;

/**
 * Strategjia Nearest Neighbor për optimizimin e rrugëve.
 * Algoritmi greedy që zgjedh gjithmonë kontejnerin më të afërt.
 * Time complexity: O(n²)
 */
public class NearestNeighborStrategy implements RouteStrategy {
    
    private static final Logger logger = Logger.getLogger(NearestNeighborStrategy.class.getName());
    
    @Override
    public List<Kontenier> calculateRoute(List<Kontenier> containers, Coordinates startPoint) {
        if (containers == null || containers.isEmpty()) {
            logger.warning("Empty container list provided to NearestNeighborStrategy");
            return Collections.emptyList();
        }
        
        logger.info(String.format("Calculating route using Nearest Neighbor for %d containers", containers.size()));
        
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
            } else {
                break; // Nuk ka më kontejnerë për të vizituar
            }
        }
        
        logger.info(String.format("Route calculated: %d containers in optimal order", route.size()));
        return route;
    }
    
    @Override
    public String getStrategyName() {
        return "NEAREST_NEIGHBOR";
    }
}

