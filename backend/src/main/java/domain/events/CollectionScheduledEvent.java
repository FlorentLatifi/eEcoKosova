package eco.kosova.domain.events;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain Event që aktivizohet kur një kontejner caktohet për mbledhje.
 * Ky event shkakton:
 * - Përditësimin e statusit të kontejnerit
 * - Njoftimin e operatorëve
 * - Përditësimin e route optimization
 */
public final class CollectionScheduledEvent implements DomainEvent {
    
    private final String eventId;
    private final String containerId;
    private final String zoneId;
    private final Instant scheduledTime;
    private final Instant occurredOn;
    
    public CollectionScheduledEvent(String containerId, String zoneId, Instant scheduledTime) {
        this.eventId = UUID.randomUUID().toString();
        this.containerId = Objects.requireNonNull(containerId, "Container ID cannot be null");
        this.zoneId = Objects.requireNonNull(zoneId, "Zone ID cannot be null");
        this.scheduledTime = Objects.requireNonNull(scheduledTime, "Scheduled time cannot be null");
        this.occurredOn = Instant.now();
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public Instant occurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getAggregateId() {
        return containerId;
    }
    
    public String getContainerId() {
        return containerId;
    }
    
    public String getZoneId() {
        return zoneId;
    }
    
    public Instant getScheduledTime() {
        return scheduledTime;
    }
    
    /**
     * Gjeneron mesazh të lexueshëm për eventin
     */
    public String getMessage() {
        return String.format(
            "Container %s in zone %s has been scheduled for collection at %s",
            containerId, zoneId, scheduledTime
        );
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionScheduledEvent that = (CollectionScheduledEvent) o;
        return Objects.equals(eventId, that.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
    
    @Override
    public String toString() {
        return String.format(
            "CollectionScheduledEvent{id='%s', container='%s', zone='%s', scheduled=%s, time=%s}",
            eventId, containerId, zoneId, scheduledTime, occurredOn
        );
    }
}