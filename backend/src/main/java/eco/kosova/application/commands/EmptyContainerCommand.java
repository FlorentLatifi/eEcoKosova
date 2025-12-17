package eco.kosova.application.commands;

import java.util.Objects;

/**
 * Command për zbrazjen e një kontejneri (pas mbledhjes).
 */
public final class EmptyContainerCommand {
    
    private final String containerId;
    
    public EmptyContainerCommand(String containerId) {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be empty");
        }
        
        this.containerId = containerId;
    }
    
    public String getContainerId() {
        return containerId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmptyContainerCommand that = (EmptyContainerCommand) o;
        return Objects.equals(containerId, that.containerId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(containerId);
    }
    
    @Override
    public String toString() {
        return String.format("EmptyContainerCommand{containerId='%s'}", containerId);
    }
}