package eco.kosova.domain.events;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain Event që aktivizohet kur një kontejner mbushet kritikisht (90%+).
 * Ky event shkakton:
 * - Dërgimin e njoftimeve
 * - Përditësimin e planeve të mbledhjes
 * - Logging për audit trail
 */
public final class ContainerFullEvent implements DomainEvent {
    
    private final String eventId;
    private final String containerId;
    private final String zoneId;
    private final int fillLevel;
    private final Instant occurredOn;
    
    public ContainerFullEvent(String containerId, String zoneId, int fillLevel) {
        this.eventId = UUID.randomUUID().toString();
        this.containerId = Objects.requireNonNull(containerId, "Container ID cannot be null");
        this.zoneId = Objects.requireNonNull(zoneId, "Zone ID cannot be null");
        this.fillLevel = fillLevel;
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
    
    public int getFillLevel() {
        return fillLevel;
    }
    
    /**
     * Merr prioritetin e eventit bazuar në nivelin e mbushjes
     */
    public String getPriority() {
        if (fillLevel >= 95) return "URGENT";
        if (fillLevel >= 90) return "HIGH";
        return "NORMAL";
    }
    
    /**
     * Gjeneron mesazh të lexueshëm për eventin
     */
    public String getMessage() {
        return String.format(
            "Container %s in zone %s is %d%% full and needs immediate attention",
            containerId, zoneId, fillLevel
        );
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerFullEvent that = (ContainerFullEvent) o;
        return Objects.equals(eventId, that.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
    
    @Override
    public String toString() {
        return String.format(
            "ContainerFullEvent{id='%s', container='%s', zone='%s', level=%d%%, time=%s}",
            eventId, containerId, zoneId, fillLevel, occurredOn
        );
    }
}