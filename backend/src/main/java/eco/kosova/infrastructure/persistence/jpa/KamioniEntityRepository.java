package eco.kosova.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KamioniEntityRepository extends JpaRepository<KamioniEntity, String> {

    Optional<KamioniEntity> findByLicensePlate(String licensePlate);

    List<KamioniEntity> findByOperatorId(String operatorId);

    List<KamioniEntity> findByStatus(String status);

    @Query("select k from KamioniEntity k where k.status = 'OPERATIONAL' and k.currentRouteId is null")
    List<KamioniEntity> findAvailable();

    @Query("select k from KamioniEntity k where k.currentRouteId = :routeId")
    List<KamioniEntity> findByRouteId(@Param("routeId") String routeId);

    @Query("select count(k) from KamioniEntity k where k.status = 'OPERATIONAL' and k.currentRouteId is null")
    long countAvailable();
}

