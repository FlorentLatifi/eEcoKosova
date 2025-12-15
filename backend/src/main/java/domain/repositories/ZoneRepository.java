package eco.kosova.domain.repositories;

import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.ZoneStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface për Zone aggregate.
 * 
 * Provides collection-like access to Zone aggregates
 * while abstracting persistence concerns.
 */
public interface ZoneRepository {
    
    /**
     * Gjen një zonë sipas ID-së
     * 
     * @param id Identifikuesi unik i zonës
     * @return Optional që përmban zonën nëse ekziston
     */
    Optional<Zone> findById(String id);
    
    /**
     * Merr të gjitha zonat
     * 
     * @return Lista e të gjitha zonave
     */
    List<Zone> findAll();
    
    /**
     * Gjen zonat sipas municipality
     * 
     * @param municipality Komuna
     * @return Lista e zonave në atë komunë
     */
    List<Zone> findByMunicipality(String municipality);
    
    /**
     * Gjen zonat me një status specifik
     * 
     * @param status Statusi i kërkuar
     * @return Lista e zonave me atë status
     */
    List<Zone> findByStatus(ZoneStatus status);
    
    /**
     * Gjen zonat që janë kritike (kanë kontejnerë kritikë)
     * 
     * @return Lista e zonave kritike
     */
    List<Zone> findCriticalZones();
    
    /**
     * Gjen zonat aktive
     * 
     * @return Lista e zonave aktive
     */
    List<Zone> findActiveZones();
    
    /**
     * Gjen zonën që përmban një kontejner specifik
     * 
     * @param containerId ID-ja e kontejnerit
     * @return Optional që përmban zonën nëse gjendet
     */
    Optional<Zone> findByContainerId(String containerId);
    
    /**
     * Ruan një zonë (create ose update)
     * 
     * @param zone Zona që do të ruhet
     * @return Zona e ruajtur
     */
    Zone save(Zone zone);
    
    /**
     * Ruan një listë të zonave (batch operation)
     * 
     * @param zones Lista e zonave
     * @return Lista e zonave të ruajtura
     */
    List<Zone> saveAll(List<Zone> zones);
    
    /**
     * Fshin një zonë sipas ID-së
     * 
     * @param id ID-ja e zonës që do të fshihet
     * @return true nëse u fshi me sukses, false nëse nuk ekzistonte
     */
    boolean deleteById(String id);
    
    /**
     * Kontrollon nëse ekziston një zonë me ID-në e dhënë
     * 
     * @param id ID-ja e zonës
     * @return true nëse ekziston, false nëse jo
     */
    boolean existsById(String id);
    
    /**
     * Numëron të gjitha zonat
     * 
     * @return Numri total i zonave
     */
    long count();
    
    /**
     * Numëron zonat aktive
     * 
     * @return Numri i zonave aktive
     */
    long countActive();
    
    /**
     * Numëron zonat kritike
     * 
     * @return Numri i zonave kritike
     */
    long countCritical();
}