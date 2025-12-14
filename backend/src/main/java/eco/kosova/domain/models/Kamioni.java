package eco.kosova.domain.models;

import eco.kosova.domain.models.valueobjects.Coordinates;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root: Kamioni
 * 
 * Përfaqëson një kamion mbledhjeje mbeturinash.
 * Trashëgon nga Paisje (sipas dokumentit konceptual).
 * 
 * Atribute specifike:
 * - targa: Targat e automjetit
 * - kapaciteti: Kapaciteti i kamionit në litra
 * - operatoriID: Operatori i caktuar
 */
public class Kamioni extends Paisje {
    
    private String licensePlate;
    private int capacity; // në litra
    private String operatorId;
    private String currentRouteId; // Rruga aktuale e caktuar
    private List<String> assignedContainerIds; // Kontejnerët e caktuar për mbledhje
    
    /**
     * Constructor për Kamioni
     */
    public Kamioni(
            String id,
            String name,
            String licensePlate,
            int capacity,
            Coordinates location,
            String operatorId,
            Instant installationDate
    ) {
        super(id, name, PaisjeStatus.OPERATIONAL, location, installationDate);
        
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be null or blank");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        if (operatorId == null || operatorId.isBlank()) {
            throw new IllegalArgumentException("Operator ID cannot be null or blank");
        }
        
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.operatorId = operatorId;
        this.assignedContainerIds = new ArrayList<>();
    }
    
    /**
     * Kontrollon nëse kamioni është i disponueshëm
     */
    public boolean isAvailable() {
        return isOperational() && currentRouteId == null;
    }
    
    /**
     * Cakton kamionin në një rrugë
     */
    public void assignToRoute(String routeId, List<String> containerIds) {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
        if (!isAvailable()) {
            throw new IllegalStateException("Kamioni nuk është i disponueshëm për caktim");
        }
        if (containerIds == null || containerIds.isEmpty()) {
            throw new IllegalArgumentException("Container IDs cannot be null or empty");
        }
        
        this.currentRouteId = routeId;
        this.assignedContainerIds = new ArrayList<>(containerIds);
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Lëshon kamionin nga rruga aktuale
     */
    public void releaseFromRoute() {
        this.currentRouteId = null;
        this.assignedContainerIds.clear();
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Kontrollon nëse kamioni ka hapësirë për më shumë kontejnerë
     */
    public boolean hasCapacity(int additionalLiters) {
        // Kjo mund të llogaritet bazuar në kontejnerët e caktuar
        // Për tani, supozojmë që kamioni ka kapacitet të mjaftueshëm
        return isAvailable() || (currentRouteId != null && additionalLiters <= capacity);
    }
    
    // Getters
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be null or blank");
        }
        this.licensePlate = licensePlate;
        this.lastUpdated = Instant.now();
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.lastUpdated = Instant.now();
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        if (operatorId == null || operatorId.isBlank()) {
            throw new IllegalArgumentException("Operator ID cannot be null or blank");
        }
        this.operatorId = operatorId;
        this.lastUpdated = Instant.now();
    }
    
    public String getCurrentRouteId() {
        return currentRouteId;
    }
    
    public List<String> getAssignedContainerIds() {
        return Collections.unmodifiableList(assignedContainerIds);
    }
    
    @Override
    public String toString() {
        return String.format("Kamioni{id='%s', name='%s', licensePlate='%s', capacity=%d, operatorId='%s', status=%s}",
                id, name, licensePlate, capacity, operatorId, status);
    }
}

