package eco.kosova.application.handlers;

import eco.kosova.application.queries.GetCriticalContainersQuery;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.repositories.KontenierRepository;

import java.util.List;

/**
 * Handler për GetCriticalContainersQuery.
 */
public class GetCriticalContainersHandler {
    
    private final KontenierRepository kontenierRepository;
    
    public GetCriticalContainersHandler(KontenierRepository kontenierRepository) {
        this.kontenierRepository = kontenierRepository;
    }
    
    public List<Kontenier> handle(GetCriticalContainersQuery query) {
        return kontenierRepository.findCriticalContainers();
    }
}