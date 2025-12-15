package eco.kosova.domain.repositories;

import eco.kosova.domain.models.Qytetari;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface për Qytetari entity.
 */
public interface QytetariRepository {
    
    Optional<Qytetari> findById(String id);
    
    List<Qytetari> findAll();
    
    Optional<Qytetari> findByName(String name);
    
    Qytetari save(Qytetari qytetari);
    
    List<Qytetari> saveAll(List<Qytetari> qytetaret);
    
    boolean deleteById(String id);
    
    boolean existsById(String id);
    
    long count();
}

