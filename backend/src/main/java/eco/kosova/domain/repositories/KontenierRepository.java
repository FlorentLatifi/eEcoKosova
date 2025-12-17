package eco.kosova.domain.repositories;

import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.valueobjects.ContainerStatus;
import eco.kosova.domain.models.valueobjects.ContainerType;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface për Kontenier aggregate.
 * 
 * Ky interface deklarohet në Domain Layer por implementohet në Infrastructure Layer.
 * Pattern: Dependency Inversion Principle (SOLID)
 * 
 * Repository Pattern benefits:
 * - Abstracts persistence concerns from domain logic
 * - Allows domain layer to remain pure
 * - Enables easy testing with mock implementations
 * - Provides collection-like interface for aggregates
 */
public interface KontenierRepository {
    
    /**
     * Gjen një kontejner sipas ID-së
     * 
     * @param id Identifikuesi unik i kontejnerit
     * @return Optional që përmban kontejnerin nëse ekziston
     */
    Optional<Kontenier> findById(String id);
    
    /**
     * Merr të gjitha kontejnerët
     * 
     * @return Lista e të gjitha kontejnerëve
     */
    List<Kontenier> findAll();
    
    /**
     * Gjen kontejnerët e një zone specifike
     * 
     * @param zoneId ID-ja e zonës
     * @return Lista e kontejnerëve në atë zonë
     */
    List<Kontenier> findByZoneId(String zoneId);
    
    /**
     * Gjen kontejnerët me një status specifik
     * 
     * @param status Statusi i kërkuar
     * @return Lista e kontejnerëve me atë status
     */
    List<Kontenier> findByStatus(ContainerStatus status);
    
    /**
     * Gjen kontejnerët me një lloj specifik
     * 
     * @param type Lloji i kontejnerit
     * @return Lista e kontejnerëve të atij lloji
     */
    List<Kontenier> findByType(ContainerType type);
    
    /**
     * Gjen kontejnerët që janë kritikë (fill level >= 90%)
     * 
     * @return Lista e kontejnerëve kritikë
     */
    List<Kontenier> findCriticalContainers();
    
    /**
     * Gjen kontejnerët që duhen mbledhur (FULL ose SCHEDULED_FOR_COLLECTION)
     * 
     * @return Lista e kontejnerëve që duhen mbledhur
     */
    List<Kontenier> findContainersNeedingCollection();
    
    /**
     * Gjen kontejnerët operativë në një zonë
     * 
     * @param zoneId ID-ja e zonës
     * @return Lista e kontejnerëve operativë
     */
    List<Kontenier> findOperationalByZoneId(String zoneId);
    
    /**
     * Gjen kontejnerët që nuk janë operativë (damage, maintenance, etc.)
     * 
     * @return Lista e kontejnerëve jo-operativë
     */
    List<Kontenier> findNonOperational();
    
    /**
     * Ruan një kontejner (create ose update)
     * 
     * @param kontenier Kontejneri që do të ruhet
     * @return Kontejneri i ruajtur
     */
    Kontenier save(Kontenier kontenier);
    
    /**
     * Ruan një listë të kontejnerëve (batch operation)
     * 
     * @param kontejner Lista e kontejnerëve
     * @return Lista e kontejnerëve të ruajtur
     */
    List<Kontenier> saveAll(List<Kontenier> kontejner);
    
    /**
     * Fshin një kontejner sipas ID-së
     * 
     * @param id ID-ja e kontejnerit që do të fshihet
     * @return true nëse u fshi me sukses, false nëse nuk ekzistonte
     */
    boolean deleteById(String id);
    
    /**
     * Kontrollon nëse ekziston një kontejner me ID-në e dhënë
     * 
     * @param id ID-ja e kontejnerit
     * @return true nëse ekziston, false nëse jo
     */
    boolean existsById(String id);
    
    /**
     * Numëron të gjitha kontejnerët
     * 
     * @return Numri total i kontejnerëve
     */
    long count();
    
    /**
     * Numëron kontejnerët në një zonë
     * 
     * @param zoneId ID-ja e zonës
     * @return Numri i kontejnerëve në atë zonë
     */
    long countByZoneId(String zoneId);
}