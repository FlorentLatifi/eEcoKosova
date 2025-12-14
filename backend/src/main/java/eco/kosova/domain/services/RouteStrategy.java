package eco.kosova.domain.services;

import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.valueobjects.Coordinates;

import java.util.List;

/**
 * Strategy interface për algoritme të ndryshme të optimizimit të rrugëve.
 * Ky është një mid-level pattern që lejon zgjedhjen e algoritmit në runtime.
 */
public interface RouteStrategy {
    
    /**
     * Llogarit rrugën optimale duke përdorur strategjinë specifike.
     * 
     * @param containers Lista e kontejnerëve për të optimizuar
     * @param startPoint Pika e fillimit (p.sh., vendndodhja e kamionit)
     * @return Lista e kontejnerëve të renditur sipas strategjisë
     */
    List<Kontenier> calculateRoute(List<Kontenier> containers, Coordinates startPoint);
    
    /**
     * Kthen emrin e strategjisë.
     * 
     * @return Emri i strategjisë
     */
    String getStrategyName();
}

