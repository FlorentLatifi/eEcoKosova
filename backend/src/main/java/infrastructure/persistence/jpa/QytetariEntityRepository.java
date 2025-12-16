package eco.kosova.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QytetariEntityRepository extends JpaRepository<QytetariEntity, String> {
    Optional<QytetariEntity> findByNameIgnoreCase(String name);
    List<QytetariEntity> findByNameContainingIgnoreCase(String name);
}

