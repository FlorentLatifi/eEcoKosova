package eco.kosova.application.handlers;

import eco.kosova.application.queries.GetAllContainersQuery;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.repositories.KontenierRepository;

import java.util.List;

/**
 * Handler për GetAllContainersQuery.
 */
public class GetAllContainersHandler {
    
    private final KontenierRepository kontenierRepository;
    
    public GetAllContainersHandler(KontenierRepository kontenierRepository) {
        this.kontenierRepository = kontenierRepository;
    }
    
    public List<Kontenier> handle(GetAllContainersQuery query) {
        return kontenierRepository.findAll();
    }
}