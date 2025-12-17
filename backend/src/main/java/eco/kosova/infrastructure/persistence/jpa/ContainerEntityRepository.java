package eco.kosova.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerEntityRepository extends JpaRepository<ContainerEntity, String> {

    List<ContainerEntity> findByZone_Id(String zoneId);

    List<ContainerEntity> findByStatus(String status);

    List<ContainerEntity> findByType(String type);

    @Query("select c from ContainerEntity c where c.fillLevel >= 90")
    List<ContainerEntity> findCriticalContainers();

    @Query("select c from ContainerEntity c where c.status in ('FULL', 'SCHEDULED_FOR_COLLECTION') and c.operational = true")
    List<ContainerEntity> findContainersNeedingCollection();

    @Query("select c from ContainerEntity c where c.zone.id = :zoneId and c.operational = true")
    List<ContainerEntity> findOperationalByZoneId(@Param("zoneId") String zoneId);

    @Query("select c from ContainerEntity c where c.operational = false")
    List<ContainerEntity> findNonOperational();

    long countByZone_Id(String zoneId);
}


