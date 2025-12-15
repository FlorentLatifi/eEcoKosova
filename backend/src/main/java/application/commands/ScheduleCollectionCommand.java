package eco.kosova.application.commands;

import java.time.Instant;
import java.util.Objects;

/**
 * Command për caktimin e një kontejneri për mbledhje.
 */
public final class ScheduleCollectionCommand {
    
    private final String containerId;
    private final Instant scheduledTime;
    
    public ScheduleCollectionCommand(String containerId, Instant scheduledTime) {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be empty");
        }
        if (scheduledTime == null) {
            throw new IllegalArgumentException("Scheduled time cannot be null");
        }
        if (scheduledTime.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Scheduled time cannot be in the past");
        }
        
        this.containerId = containerId;
        this.scheduledTime = scheduledTime;
    }
    
    public String getContainerId() {
        return containerId;
    }
    
    public Instant getScheduledTime() {
        return scheduledTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleCollectionCommand that = (ScheduleCollectionCommand) o;
        return Objects.equals(containerId, that.containerId) &&
               Objects.equals(scheduledTime, that.scheduledTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(containerId, scheduledTime);
    }
    
    @Override
    public String toString() {
        return String.format(
            "ScheduleCollectionCommand{containerId='%s', scheduledTime=%s}",
            containerId, scheduledTime
        );
    }
}