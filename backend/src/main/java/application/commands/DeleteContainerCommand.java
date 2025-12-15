package eco.kosova.application.commands;

import java.util.Objects;

/**
 * Command për fshirjen e një kontejneri.
 */
public final class DeleteContainerCommand {
    
    private final String containerId;
    
    public DeleteContainerCommand(String containerId) {
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
        DeleteContainerCommand that = (DeleteContainerCommand) o;
        return Objects.equals(containerId, that.containerId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(containerId);
    }
    
    @Override
    public String toString() {
        return "DeleteContainerCommand{containerId='" + containerId + "'}";
    }
}

