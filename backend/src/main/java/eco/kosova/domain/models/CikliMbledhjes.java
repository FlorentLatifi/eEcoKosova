package eco.kosova.domain.models;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Aggregate Root: CikliMbledhjes
 * 
 * Përfaqëson orarin dhe parametrat e mbledhjes së mbetjeve.
 * Sipas dokumentit konceptual.
 * 
 * Atribute:
 * - orari: DateTime - Koha e mbledhjes
 * - kapacitetiMax: int - Kapaciteti maksimal i mbledhjes
 * - dita: Dita[] - Ditët e javës për mbledhje
 */
public class CikliMbledhjes {
    
    private final String id;
    private LocalDateTime scheduleTime; // Koha e mbledhjes
    private int maxCapacity; // Kapaciteti maksimal në litra
    private Set<DayOfWeek> collectionDays; // Ditët e javës për mbledhje
    private String zoneId; // Zona për të cilën është cikli
    private String kamioniId; // Kamioni i caktuar
    private CikliStatus status;
    private Instant createdAt;
    private Instant lastUpdated;
    
    /**
     * Constructor për CikliMbledhjes
     */
    public CikliMbledhjes(
            String id,
            LocalDateTime scheduleTime,
            int maxCapacity,
            Set<DayOfWeek> collectionDays,
            String zoneId
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Cikli ID cannot be null or blank");
        }
        if (scheduleTime == null) {
            throw new IllegalArgumentException("Schedule time cannot be null");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Max capacity must be greater than 0");
        }
        if (collectionDays == null || collectionDays.isEmpty()) {
            throw new IllegalArgumentException("Collection days cannot be null or empty");
        }
        if (zoneId == null || zoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be null or blank");
        }
        
        this.id = id;
        this.scheduleTime = scheduleTime;
        this.maxCapacity = maxCapacity;
        this.collectionDays = new HashSet<>(collectionDays);
        this.zoneId = zoneId;
        this.status = CikliStatus.SCHEDULED;
        this.createdAt = Instant.now();
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Kontrollon nëse cikli është aktiv për ditën e dhënë
     */
    public boolean isActiveForDay(DayOfWeek day) {
        return collectionDays.contains(day);
    }
    
    /**
     * Kontrollon nëse cikli është aktiv për datën e dhënë
     */
    public boolean isActiveForDate(LocalDateTime date) {
        return isActiveForDay(date.getDayOfWeek()) && 
               status == CikliStatus.SCHEDULED &&
               date.isAfter(scheduleTime.minusHours(1)); // Lejohet 1 orë para
    }
    
    /**
     * Cakton kamionin për ciklin
     */
    public void assignKamioni(String kamioniId) {
        if (kamioniId == null || kamioniId.isBlank()) {
            throw new IllegalArgumentException("Kamioni ID cannot be null or blank");
        }
        this.kamioniId = kamioniId;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Aktivizon ciklin
     */
    public void activate() {
        if (kamioniId == null || kamioniId.isBlank()) {
            throw new IllegalStateException("Kamioni duhet të jetë i caktuar para aktivizimit");
        }
        this.status = CikliStatus.ACTIVE;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Kompleton ciklin
     */
    public void complete() {
        this.status = CikliStatus.COMPLETED;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Anulon ciklin
     */
    public void cancel() {
        this.status = CikliStatus.CANCELLED;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Shton ditë mbledhjeje
     */
    public void addCollectionDay(DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }
        this.collectionDays.add(day);
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Heq ditë mbledhjeje
     */
    public void removeCollectionDay(DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }
        if (collectionDays.size() <= 1) {
            throw new IllegalStateException("Cikli duhet të ketë të paktën një ditë mbledhjeje");
        }
        this.collectionDays.remove(day);
        this.lastUpdated = Instant.now();
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }
    
    public void setScheduleTime(LocalDateTime scheduleTime) {
        if (scheduleTime == null) {
            throw new IllegalArgumentException("Schedule time cannot be null");
        }
        this.scheduleTime = scheduleTime;
        this.lastUpdated = Instant.now();
    }
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    public void setMaxCapacity(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Max capacity must be greater than 0");
        }
        this.maxCapacity = maxCapacity;
        this.lastUpdated = Instant.now();
    }
    
    public Set<DayOfWeek> getCollectionDays() {
        return Collections.unmodifiableSet(collectionDays);
    }
    
    public String getZoneId() {
        return zoneId;
    }
    
    public String getKamioniId() {
        return kamioniId;
    }
    
    public CikliStatus getStatus() {
        return status;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getLastUpdated() {
        return lastUpdated;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CikliMbledhjes that = (CikliMbledhjes) o;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("CikliMbledhjes{id='%s', scheduleTime=%s, maxCapacity=%d, zoneId='%s', status=%s}",
                id, scheduleTime, maxCapacity, zoneId, status);
    }
    
    /**
     * Enum për statusin e ciklit
     */
    public enum CikliStatus {
        SCHEDULED("I Planifikuar"),
        ACTIVE("Aktiv"),
        COMPLETED("I Kompletuar"),
        CANCELLED("I Anuluar");
        
        private final String displayName;
        
        CikliStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

