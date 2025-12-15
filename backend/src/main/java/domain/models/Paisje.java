package eco.kosova.domain.models;

import eco.kosova.domain.models.valueobjects.Coordinates;

import java.time.Instant;

/**
 * Abstract base class për të gjitha paisjet në sistem.
 * Sipas dokumentit konceptual, Paisje është klasa bazë për Kontenier dhe Kamioni.
 * 
 * Atribute:
 * - paisjeID: Identifikues unik
 * - emri: Emri i pajisjes
 * - statusi: Statusi operacional
 * - vendndodhja: Vendndodhja gjeografike
 * - dataInstalimit: Data e instalimit/blerjes
 */
public abstract class Paisje {
    
    protected final String id;
    protected String name;
    protected PaisjeStatus status;
    protected Coordinates location;
    protected Instant installationDate;
    protected Instant lastUpdated;
    
    /**
     * Constructor për Paisje
     */
    protected Paisje(
            String id,
            String name,
            PaisjeStatus status,
            Coordinates location,
            Instant installationDate
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Paisje ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Paisje name cannot be null or blank");
        }
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        
        this.id = id;
        this.name = name;
        this.status = status != null ? status : PaisjeStatus.OPERATIONAL;
        this.location = location;
        this.installationDate = installationDate != null ? installationDate : Instant.now();
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Merr identifikuesin e paisjes
     */
    public String getId() {
        return id;
    }
    
    /**
     * Merr emrin e paisjes
     */
    public String getName() {
        return name;
    }
    
    /**
     * Përditëson emrin
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Merr statusin e paisjes
     */
    public PaisjeStatus getStatus() {
        return status;
    }
    
    /**
     * Përditëson statusin e paisjes
     */
    public void updateStatus(PaisjeStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Merr vendndodhjen
     */
    public Coordinates getLocation() {
        return location;
    }
    
    /**
     * Përditëson vendndodhjen
     */
    public void setLocation(Coordinates location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        this.location = location;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Merr datën e instalimit
     */
    public Instant getInstallationDate() {
        return installationDate;
    }
    
    /**
     * Merr datën e përditësimit të fundit
     */
    public Instant getLastUpdated() {
        return lastUpdated;
    }
    
    /**
     * Kontrollon nëse paisja është operacionale
     */
    public boolean isOperational() {
        return status == PaisjeStatus.OPERATIONAL;
    }
    
    /**
     * Kontrollon nëse paisja është në mirëmbajtje
     */
    public boolean isInMaintenance() {
        return status == PaisjeStatus.MAINTENANCE;
    }
    
    /**
     * Kontrollon nëse paisja është jashtë shërbimi
     */
    public boolean isOutOfService() {
        return status == PaisjeStatus.OUT_OF_SERVICE;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paisje paisje = (Paisje) o;
        return id.equals(paisje.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Paisje{id='%s', name='%s', status=%s}", id, name, status);
    }
    
    /**
     * Enum për statusin e paisjes
     */
    public enum PaisjeStatus {
        OPERATIONAL("Operativ"),
        MAINTENANCE("Në Mirëmbajtje"),
        OUT_OF_SERVICE("Jashtë Shërbimi");
        
        private final String displayName;
        
        PaisjeStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

