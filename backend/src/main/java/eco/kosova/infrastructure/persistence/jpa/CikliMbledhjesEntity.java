package eco.kosova.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CikletMbledhjes")
public class CikliMbledhjesEntity {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false)
    private LocalDateTime scheduleTime;

    @Column(nullable = false)
    private int maxCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zoneId", nullable = false)
    private ZoneEntity zone;

    @Column(length = 50)
    private String kamioniId;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant lastUpdated;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CikliCollectionDays", joinColumns = @JoinColumn(name = "cikliId"))
    @Column(name = "dayOfWeek")
    private Set<String> collectionDays = new HashSet<>();

    public CikliMbledhjesEntity() {
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public ZoneEntity getZone() {
        return zone;
    }

    public void setZone(ZoneEntity zone) {
        this.zone = zone;
    }

    public String getKamioniId() {
        return kamioniId;
    }

    public void setKamioniId(String kamioniId) {
        this.kamioniId = kamioniId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<String> getCollectionDays() {
        return collectionDays;
    }

    public void setCollectionDays(Set<String> collectionDays) {
        this.collectionDays = collectionDays;
    }
}

