package eco.kosova.domain.models;

import eco.kosova.domain.events.CollectionScheduledEvent;
import eco.kosova.domain.events.ContainerFullEvent;
import eco.kosova.domain.events.DomainEvent;
import eco.kosova.domain.models.valueobjects.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Aggregate Root: Kontenier
 * 
 * Përfaqëson një kontejner mbeturinash në sistem.
 * Si aggregate root, Kontenier është përgjegjës për:
 * - Mbrojtjen e rregullave të biznesit
 * - Gjenerimin e domain events
 * - Ruajtjen e konsistencës së të dhënave
 * 
 * Business Rules:
 * - Kontejneri nuk mund të ketë fill level > 100%
 * - Eventi ContainerFullEvent gjenerohet kur fill level >= 90%
 * - Kontejneri nuk mund të caktohet për mbledhje nëse është në mirëmbajtje
 * - Çdo ndryshim i rëndësishëm gjeneron një domain event
 */
public class Kontenier {
    
    // Identity
    private final String id;
    private String zoneId;
    
    // Value Objects
    private FillLevel fillLevel;
    private Coordinates location;
    private Address address;
    
    // Enums
    private ContainerType type;
    private ContainerStatus status;
    
    // Properties
    private int capacity; // në litra
    private boolean operational;
    private Instant lastEmptied;
    private Instant lastUpdated;
    
    // Domain Events (të pa-publikuara)
    private final Collection<DomainEvent> domainEvents;
    
    // Metadata
    private final Instant createdAt;
    private Instant modifiedAt;
    
    /**
     * Constructor kryesor për krijimin e një kontejneri të ri
     */
    public Kontenier(
            String id,
            String zoneId,
            ContainerType type,
            int capacity,
            Coordinates location,
            Address address
    ) {
        // Validation
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be empty");
        }
        if (zoneId == null || zoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be empty");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        
        this.id = id;
        this.zoneId = zoneId;
        this.type = Objects.requireNonNull(type, "Container type cannot be null");
        this.capacity = capacity;
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        
        // Initial state
        this.fillLevel = new FillLevel(0);
        this.status = ContainerStatus.OPERATIONAL;
        this.operational = true;
        this.domainEvents = new ArrayList<>();
        
        // Timestamps
        this.createdAt = Instant.now();
        this.lastUpdated = Instant.now();
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Constructor për reconstruction nga persistence
     */
    public Kontenier(
            String id,
            String zoneId,
            ContainerType type,
            int capacity,
            Coordinates location,
            Address address,
            FillLevel fillLevel,
            ContainerStatus status,
            boolean operational,
            Instant lastEmptied,
            Instant createdAt
    ) {
        this.id = id;
        this.zoneId = zoneId;
        this.type = type;
        this.capacity = capacity;
        this.location = location;
        this.address = address;
        this.fillLevel = fillLevel;
        this.status = status;
        this.operational = operational;
        this.lastEmptied = lastEmptied;
        this.createdAt = createdAt;
        this.lastUpdated = Instant.now();
        this.modifiedAt = Instant.now();
        this.domainEvents = new ArrayList<>();
    }
    
    // ==================== BUSINESS METHODS ====================
    
    /**
     * Përditëson nivelin e mbushjes së kontejnerit.
     * Gjeneron ContainerFullEvent nëse niveli bëhet kritik.
     * 
     * @param newLevel Niveli i ri (0-100)
     */
    public void updateFillLevel(int newLevel) {
        FillLevel oldFillLevel = this.fillLevel;
        FillLevel newFillLevel = new FillLevel(newLevel);
        
        // Kontrollo nëse niveli është bërë kritik
        if (newFillLevel.isCritical() && !oldFillLevel.isCritical()) {
            // Gjenero event për kontejner të mbushur
            this.addDomainEvent(new ContainerFullEvent(
                this.id,
                this.zoneId,
                newLevel
            ));
            
            // Ndrysho statusin në FULL
            if (this.status == ContainerStatus.OPERATIONAL) {
                this.status = ContainerStatus.FULL;
            }
        }
        
        this.fillLevel = newFillLevel;
        this.lastUpdated = Instant.now();
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Zbraz kontejnerin (pas mbledhjes).
     * Reset fill level në 0 dhe përditëso statusin.
     */
    public void empty() {
        if (!this.operational) {
            throw new IllegalStateException(
                String.format("Cannot empty container %s: not operational", this.id)
            );
        }
        
        this.fillLevel = new FillLevel(0);
        this.status = ContainerStatus.OPERATIONAL;
        this.lastEmptied = Instant.now();
        this.lastUpdated = Instant.now();
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Cakton kontejnerin për mbledhje.
     * Gjeneron CollectionScheduledEvent.
     * 
     * @param scheduledTime Koha e planifikuar për mbledhje
     */
    public void scheduleCollection(Instant scheduledTime) {
        if (!canScheduleCollection()) {
            throw new IllegalStateException(
                String.format("Cannot schedule collection for container %s: status is %s",
                    this.id, this.status)
            );
        }
        
        this.status = ContainerStatus.SCHEDULED_FOR_COLLECTION;
        this.modifiedAt = Instant.now();
        
        this.addDomainEvent(new CollectionScheduledEvent(
            this.id,
            this.zoneId,
            scheduledTime
        ));
    }
    
    /**
     * Vendos kontejnerin në mirëmbajtje
     */
    public void markUnderMaintenance() {
        this.status = ContainerStatus.UNDER_MAINTENANCE;
        this.operational = false;
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Vendos kontejnerin si të dëmtuar
     */
    public void markAsDamaged() {
        this.status = ContainerStatus.DAMAGED;
        this.operational = false;
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Riaktivizo kontejnerin (pas mirëmbajtjes)
     */
    public void reactivate() {
        if (this.status == ContainerStatus.DAMAGED) {
            throw new IllegalStateException(
                String.format("Cannot reactivate damaged container %s", this.id)
            );
        }
        
        this.status = ContainerStatus.OPERATIONAL;
        this.operational = true;
        this.modifiedAt = Instant.now();
    }
    
    /**
     * Zhvendos kontejnerin në një zonë tjetër
     */
    public void relocateToZone(String newZoneId) {
        if (newZoneId == null || newZoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be empty");
        }
        
        if (!this.zoneId.equals(newZoneId)) {
            this.zoneId = newZoneId;
            this.modifiedAt = Instant.now();
        }
    }
    
    /**
     * Përditëso vendndodhjen GPS të kontejnerit
     */
    public void updateLocation(Coordinates newLocation) {
        this.location = Objects.requireNonNull(newLocation, "Location cannot be null");
        this.modifiedAt = Instant.now();
    }
    
    // ==================== BUSINESS RULES (Private Methods) ====================
    
    /**
     * Kontrollon nëse kontejneri mund të caktohet për mbledhje
     */
    private boolean canScheduleCollection() {
        return this.operational &&
               this.status != ContainerStatus.UNDER_MAINTENANCE &&
               this.status != ContainerStatus.DAMAGED &&
               this.status != ContainerStatus.OUT_OF_SERVICE;
    }
    
    // ==================== DOMAIN EVENTS MANAGEMENT ====================
    
    /**
     * Shto një domain event në listën e eventeve të pa-publikuara
     */
    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    /**
     * Merr të gjitha domain events që janë gjeneruar
     * (për t'u publikuar nga Application Layer)
     */
    public Collection<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableCollection(domainEvents);
    }
    
    /**
     * Pastro domain events pas publikimit
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
    
    // ==================== QUERY METHODS ====================
    
    /**
     * Kontrollon nëse kontejneri ka nevojë për mbledhje urgjente
     */
    public boolean needsUrgentCollection() {
        return this.fillLevel.isCritical() && this.operational;
    }
    
    /**
     * Kontrollon nëse kontejneri është gati për mbledhje
     */
    public boolean isReadyForCollection() {
        return this.operational &&
               (this.status == ContainerStatus.FULL ||
                this.status == ContainerStatus.SCHEDULED_FOR_COLLECTION);
    }
    
    /**
     * Llogarit ditët që nga zbrazja e fundit
     */
    public long daysSinceLastEmptied() {
        if (lastEmptied == null) {
            return -1; // Nuk është zbrazitur kurrë
        }
        
        return java.time.Duration.between(lastEmptied, Instant.now()).toDays();
    }
    
    /**
     * Llogarit shpejtësinë mesatare të mbushjes (% per ditë)
     */
    public double getAverageFillRate() {
        long daysSinceEmpty = daysSinceLastEmptied();
        if (daysSinceEmpty <= 0) {
            return 0.0;
        }
        
        return (double) fillLevel.getValue() / daysSinceEmpty;
    }
    
    /**
     * Parashikon kohën derisa kontejneri të mbushet (në ditë)
     */
    public long predictDaysUntilFull() {
        double fillRate = getAverageFillRate();
        if (fillRate <= 0) {
            return -1; // Nuk mund të parashikohet
        }
        
        int remainingCapacity = 100 - fillLevel.getValue();
        return (long) Math.ceil(remainingCapacity / fillRate);
    }
    
    // ==================== GETTERS ====================
    
    public String getId() {
        return id;
    }
    
    public String getZoneId() {
        return zoneId;
    }
    
    public FillLevel getFillLevel() {
        return fillLevel;
    }
    
    public Coordinates getLocation() {
        return location;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public ContainerType getType() {
        return type;
    }
    
    public ContainerStatus getStatus() {
        return status;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public boolean isOperational() {
        return operational;
    }
    
    public Instant getLastEmptied() {
        return lastEmptied;
    }
    
    public Instant getLastUpdated() {
        return lastUpdated;
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
        Kontenier kontenier = (Kontenier) o;
        return Objects.equals(id, kontenier.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format(
            "Kontenier{id='%s', zone='%s', type=%s, fillLevel=%d%%, status=%s, operational=%b}",
            id, zoneId, type.getDisplayName(), fillLevel.getValue(), 
            status.getDisplayName(), operational
        );
    }
}