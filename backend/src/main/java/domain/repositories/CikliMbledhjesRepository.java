package eco.kosova.domain.repositories;

import eco.kosova.domain.models.CikliMbledhjes;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface për CikliMbledhjes aggregate.
 */
public interface CikliMbledhjesRepository {
    
    Optional<CikliMbledhjes> findById(String id);
    
    List<CikliMbledhjes> findAll();
    
    List<CikliMbledhjes> findByZoneId(String zoneId);
    
    List<CikliMbledhjes> findByKamioniId(String kamioniId);
    
    List<CikliMbledhjes> findByStatus(CikliMbledhjes.CikliStatus status);
    
    List<CikliMbledhjes> findActive();
    
    List<CikliMbledhjes> findByCollectionDay(DayOfWeek day);
    
    CikliMbledhjes save(CikliMbledhjes cikli);
    
    List<CikliMbledhjes> saveAll(List<CikliMbledhjes> ciklet);
    
    boolean deleteById(String id);
    
    boolean existsById(String id);
    
    long count();
    
    long countByZoneId(String zoneId);
}

