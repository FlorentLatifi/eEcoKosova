package eco.kosova.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneEntityRepository extends JpaRepository<ZoneEntity, String> {

    List<ZoneEntity> findByMunicipalityIgnoreCase(String municipality);

    List<ZoneEntity> findByStatus(String status);

    @Query("select z from ZoneEntity z where z.status = 'CRITICAL'")
    List<ZoneEntity> findCriticalZones();

    @Query("select z from ZoneEntity z where z.status = 'ACTIVE'")
    List<ZoneEntity> findActiveZones();

    @Query("select z from ZoneEntity z join z.containers c where c.id = :containerId")
    Optional<ZoneEntity> findByContainerId(@Param("containerId") String containerId);

    @Query("select count(z) from ZoneEntity z where z.status = 'ACTIVE'")
    long countActive();

    @Query("select count(z) from ZoneEntity z where z.status = 'CRITICAL'")
    long countCritical();
}


