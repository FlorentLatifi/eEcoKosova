package eco.kosova.application.handlers;

import eco.kosova.application.queries.GetContainerByIdQuery;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.repositories.KontenierRepository;

import java.util.Optional;

/**
 * Handler për GetContainerByIdQuery.
 * 
 * Query Handlers:
 * - Read-only operations
 * - No side effects
 * - Return data from repositories
 */
public class GetContainerByIdHandler {
    
    private final KontenierRepository kontenierRepository;
    
    public GetContainerByIdHandler(KontenierRepository kontenierRepository) {
        this.kontenierRepository = kontenierRepository;
    }
    
    public Optional<Kontenier> handle(GetContainerByIdQuery query) {
        return kontenierRepository.findById(query.getContainerId());
    }
}