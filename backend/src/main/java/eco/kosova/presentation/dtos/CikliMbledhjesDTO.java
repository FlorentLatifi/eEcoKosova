package eco.kosova.presentation.dtos;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO për CikliMbledhjes aggregate.
 */
public class CikliMbledhjesDTO {
    private String id;
    private LocalDateTime scheduleTime;
    private int maxCapacity;
    private Set<DayOfWeek> collectionDays;
    private String zoneId;
    private String kamioniId;
    private String status;
    private Instant createdAt;
    private Instant lastUpdated;
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public LocalDateTime getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(LocalDateTime scheduleTime) { this.scheduleTime = scheduleTime; }
    
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    
    public Set<DayOfWeek> getCollectionDays() { return collectionDays; }
    public void setCollectionDays(Set<DayOfWeek> collectionDays) { this.collectionDays = collectionDays; }
    
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    
    public String getKamioniId() { return kamioniId; }
    public void setKamioniId(String kamioniId) { this.kamioniId = kamioniId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
}

