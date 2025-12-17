package eco.kosova.domain.services;

import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.valueobjects.Coordinates;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Strategjia Priority-Based për optimizimin e rrugëve.
 * Rendit kontejnerët sipas nivelit të mbushjes (më të mbushurit së pari).
 */
public class PriorityBasedStrategy implements RouteStrategy {
    
    private static final Logger logger = Logger.getLogger(PriorityBasedStrategy.class.getName());
    
    @Override
    public List<Kontenier> calculateRoute(List<Kontenier> containers, Coordinates startPoint) {
        if (containers == null || containers.isEmpty()) {
            logger.warning("Empty container list provided to PriorityBasedStrategy");
            return Collections.emptyList();
        }
        
        logger.info(String.format("Calculating route using Priority-Based for %d containers", containers.size()));
        
        // Rendit kontejnerët sipas nivelit të mbushjes (descending)
        List<Kontenier> route = containers.stream()
            .sorted(Comparator.comparingInt(c -> -c.getFillLevel().getValue()))
            .collect(Collectors.toList());
        
        logger.info(String.format("Route calculated: %d containers sorted by priority", route.size()));
        return route;
    }
    
    @Override
    public String getStrategyName() {
        return "PRIORITY_BASED";
    }
}

