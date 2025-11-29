package eco.kosova.application.handlers;

import eco.kosova.application.queries.GetContainersByZoneQuery;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.repositories.KontenierRepository;

import java.util.List;

/**
 * Handler për GetContainersByZoneQuery.
 */
public class GetContainersByZoneHandler {
    
    private final KontenierRepository kontenierRepository;
    
    public GetContainersByZoneHandler(KontenierRepository kontenierRepository) {
        this.kontenierRepository = kontenierRepository;
    }
    
    public List<Kontenier> handle(GetContainersByZoneQuery query) {
        return kontenierRepository.findByZoneId(query.getZoneId());
    }
}