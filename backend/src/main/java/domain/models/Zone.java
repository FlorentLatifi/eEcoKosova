package eco.kosova.domain.models;

import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.models.valueobjects.ZoneStatus;

import java.time.Instant;
import java.util.*;

/**
 * Aggregate Root: Zone
 * 
 * Përfaqëson një zonë gjeografike që përmban kontejnerë mbeturinash.
 * Si aggregate root, Zone është përgjegjës për:
 * - Menaxhimin e kontejnerëve brenda zonës
 * - Llogaritjen e statistikave për zonën
 * - Monitorimin e gjendjes kritike
 * 
 * Business Rules:
 * - Zona duhet të ketë të paktën 1 kontejner
 * - Zona bëhet CRITICAL kur >50% e kontejnerëve janë plot
 * - Kontejnerët nuk mund të duplikohen brenda zonës
 */
public class Zone {
    
    // Identity
    private final String id;
    private String name;
    
    // Value Objects
    private Coordinates centerPoint;
    
    // Enums
    private ZoneStatus status;
    
    // Collections
    private final Set<String> containerIds; // ID-të e kontejnerëve në zonë
    
    // Properties
    private int criticalThreshold; // Sa % e kontejnerëve duhet të jenë plot për CRITICAL
    private String municipality;
    private String description;
    
    // Metadata
    private final Instant createdAt;
    private Instant modifiedAt;
    
    /**
     * Constructor kryesor për krijimin e një zone të re
     */
    public Zone(
            String id,
            String name,
            Coordinates centerPoint,
            String municipality
    ) {
        // Validation
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Zone name cannot be empty");
        }
        
        this.id = id;
        this.name = name;
        this.centerPoint = Objects.requireNonNull(centerPoint, "Center point cannot be null");
        this.municipality = Objects.requireNonNull(municipality, "Municipality cannot be null");
        
        // Initial state
        this.status = ZoneStatus.ACTIVE;
        this.containerIds = new HashSet<>();
        this.criticalThreshold = 50; // Default: 50% e kontejnerëve plot = CRITICAL
        
        // Timestamps
        this.createdAt = Instant.now();
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Constructor për reconstruction nga persistence
     */
    public Zone(
            String id,
            String name,
            Coordinates centerPoint,
            String municipality,
            ZoneStatus status,
            Set<String> containerIds,
            int criticalThreshold,
            String description,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.centerPoint = centerPoint;
        this.municipality = municipality;
        this.status = status;
        this.containerIds = new HashSet<>(containerIds);
        this.criticalThreshold = criticalThreshold;
        this.description = description;
        this.createdAt = createdAt;
        this.modifiedAt = Instant.now();
    }
    
    // ==================== BUSINESS METHODS ====================
    
    /**
     * Shto një kontejner në zonë
     */
    public void addContainer(String containerId) {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be empty");
        }
        
        if (containerIds.contains(containerId)) {
            throw new IllegalArgumentException(
                String.format("Container %s already exists in zone %s", containerId, this.id)
            );
        }
        
        containerIds.add(containerId);
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Hiq një kontejner nga zona
     */
    public void removeContainer(String containerId) {
        if (!containerIds.contains(containerId)) {
            throw new IllegalArgumentException(
                String.format("Container %s does not exist in zone %s", containerId, this.id)
            );
        }
        
        containerIds.remove(containerId);
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Kontrollon nëse zona përmban një kontejner specifik
     */
    public boolean containsContainer(String containerId) {
        return containerIds.contains(containerId);
    }
    
    /**
     * Përditëso statusin e zonës bazuar në gjendjen e kontejnerëve
     * 
     * @param criticalCount Numri i kontejnerëve kritikë
     * @param totalCount Numri total i kontejnerëve në zonë
     */
    public void updateStatusBasedOnContainers(int criticalCount, int totalCount) {
        if (totalCount == 0) {
            this.status = ZoneStatus.INACTIVE;
            return;
        }
        
        double criticalPercentage = (double) criticalCount / totalCount * 100;
        
        if (criticalPercentage >= criticalThreshold) {
            this.status = ZoneStatus.CRITICAL;
        } else {
            this.status = ZoneStatus.ACTIVE;
        }
        
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Vendos zonën në mirëmbajtje
     */
    public void markUnderMaintenance() {
        this.status = ZoneStatus.UNDER_MAINTENANCE;
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Riaktivizo zonën
     */
    public void activate() {
        if (containerIds.isEmpty()) {
            throw new IllegalStateException(
                String.format("Cannot activate zone %s: no containers", this.id)
            );
        }
        
        this.status = ZoneStatus.ACTIVE;
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Çaktivizo zonën përkohësisht
     */
    public void deactivate() {
        this.status = ZoneStatus.INACTIVE;
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Përditëso threshold-in kritik
     */
    public void updateCriticalThreshold(int newThreshold) {
        if (newThreshold < 0 || newThreshold > 100) {
            throw new IllegalArgumentException(
                "Critical threshold must be between 0 and 100"
            );
        }
        
        this.criticalThreshold = newThreshold;
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Përditëso emrin e zonës
     */
    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Zone name cannot be empty");
        }
        
        this.name = newName;
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Përditëso center point të zonës
     */
    public void updateCenterPoint(Coordinates newCenterPoint) {
        this.centerPoint = Objects.requireNonNull(newCenterPoint, "Center point cannot be null");
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Shto përshkrim për zonën
     */
    public void setDescription(String description) {
        this.description = description;
        this.modifiedAt = Instant.now();
    }
    
    // ==================== QUERY METHODS ====================
    
    /**
     * Merr numrin e kontejnerëve në zonë
     */
    public int getContainerCount() {
        return containerIds.size();
    }
    
    /**
     * Kontrollon nëse zona ka kontejnerë
     */
    public boolean hasContainers() {
        return !containerIds.isEmpty();
    }
    
    /**
     * Kontrollon nëse zona është aktive
     */
    public boolean isActive() {
        return status == ZoneStatus.ACTIVE;
    }
    
    /**
     * Kontrollon nëse zona ka nevojë për vëmendje
     */
    public boolean needsAttention() {
        return status.needsAttention();
    }
    
    /**
     * Merr një kopje të read-only të container IDs
     */
    public Set<String> getContainerIds() {
        return Collections.unmodifiableSet(containerIds);
    }
    
    // ==================== GETTERS ====================
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Coordinates getCenterPoint() {
        return centerPoint;
    }
    
    public ZoneStatus getStatus() {
        return status;
    }
    
    public int getCriticalThreshold() {
        return criticalThreshold;
    }
    
    public String getMunicipality() {
        return municipality;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getModifiedAt() {
        return modifiedAt;
    }
    
    // ==================== EQUALS, HASHCODE, TOSTRING ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return Objects.equals(id, zone.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format(
            "Zone{id='%s', name='%s', municipality='%s', status=%s, containers=%d}",
            id, name, municipality, status.getDisplayName(), containerIds.size()
        );
    }
}