package eco.kosova.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CikliMbledhjesEntityRepository extends JpaRepository<CikliMbledhjesEntity, String> {
    List<CikliMbledhjesEntity> findByZone_Id(String zoneId);
    List<CikliMbledhjesEntity> findByStatus(String status);
    
    @Query("SELECT c FROM CikliMbledhjesEntity c WHERE c.status IN ('ACTIVE', 'SCHEDULED')")
    List<CikliMbledhjesEntity> findActiveCiklet();
    
    List<CikliMbledhjesEntity> findByKamioniId(String kamioniId);
}

