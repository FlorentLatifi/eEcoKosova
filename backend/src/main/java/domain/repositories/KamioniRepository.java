package eco.kosova.domain.repositories;

import eco.kosova.domain.models.Kamioni;
import eco.kosova.domain.models.Paisje;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface për Kamioni aggregate.
 */
public interface KamioniRepository {
    
    Optional<Kamioni> findById(String id);
    
    List<Kamioni> findAll();
    
    List<Kamioni> findByOperatorId(String operatorId);
    
    List<Kamioni> findAvailable();
    
    List<Kamioni> findByStatus(Paisje.PaisjeStatus status);
    
    Optional<Kamioni> findByLicensePlate(String licensePlate);
    
    List<Kamioni> findByRouteId(String routeId);
    
    Kamioni save(Kamioni kamioni);
    
    List<Kamioni> saveAll(List<Kamioni> kamionet);
    
    boolean deleteById(String id);
    
    boolean existsById(String id);
    
    long count();
    
    long countAvailable();
}

