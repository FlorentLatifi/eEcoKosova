package eco.kosova.application.commands;

import java.util.Objects;

/**
 * Command për përditësimin e nivelit të mbushjes së një kontejneri.
 * 
 * CQRS Pattern: Commands përfaqësojnë intent për të ndryshuar gjendjen e sistemit.
 * 
 * Commands janë:
 * - Immutable
 * - Self-validating
 * - Express business intent
 */
public final class UpdateContainerFillLevelCommand {
    
    private final String containerId;
    private final int fillLevel;
    
    public UpdateContainerFillLevelCommand(String containerId, int fillLevel) {
        // Validation
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be empty");
        }
        if (fillLevel < 0 || fillLevel > 100) {
            throw new IllegalArgumentException(
                String.format("Fill level must be between 0-100. Received: %d", fillLevel)
            );
        }
        
        this.containerId = containerId;
        this.fillLevel = fillLevel;
    }
    
    public String getContainerId() {
        return containerId;
    }
    
    public int getFillLevel() {
        return fillLevel;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateContainerFillLevelCommand that = (UpdateContainerFillLevelCommand) o;
        return fillLevel == that.fillLevel && Objects.equals(containerId, that.containerId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(containerId, fillLevel);
    }
    
    @Override
    public String toString() {
        return String.format(
            "UpdateContainerFillLevelCommand{containerId='%s', fillLevel=%d}",
            containerId, fillLevel
        );
    }
}