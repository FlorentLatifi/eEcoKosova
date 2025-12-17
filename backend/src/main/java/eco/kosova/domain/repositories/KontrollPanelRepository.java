package eco.kosova.domain.repositories;

import eco.kosova.domain.models.KontrollPanel;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface për KontrollPanel entity.
 */
public interface KontrollPanelRepository {
    
    Optional<KontrollPanel> findById(String id);
    
    List<KontrollPanel> findAll();
    
    Optional<KontrollPanel> findByQytetariId(String qytetariId);
    
    List<KontrollPanel> findByLanguage(String language);
    
    List<KontrollPanel> findByTheme(String theme);
    
    KontrollPanel save(KontrollPanel panel);
    
    List<KontrollPanel> saveAll(List<KontrollPanel> panels);
    
    boolean deleteById(String id);
    
    boolean existsById(String id);
    
    long count();
}

