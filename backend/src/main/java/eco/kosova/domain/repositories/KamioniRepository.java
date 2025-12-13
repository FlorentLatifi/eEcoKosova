package eco.kosova.domain.repositories;

import eco.kosova.domain.models.Kamioni;
import java.util.List;
import java.util.Optional;

public interface KamioniRepository {
    Optional<Kamioni> findById(String id);
    List<Kamioni> findAll();
    List<Kamioni> findByOperatorId(String operatorId);
    Kamioni save(Kamioni kamioni);
    boolean deleteById(String id);
    boolean existsById(String id);
    long count();
}
