package eco.kosova.application.commands;

import java.util.Objects;

/**
 * Command për fshirjen e një zone.
 */
public final class DeleteZoneCommand {
    
    private final String zoneId;
    
    public DeleteZoneCommand(String zoneId) {
        if (zoneId == null || zoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be empty");
        }
        this.zoneId = zoneId;
    }
    
    public String getZoneId() {
        return zoneId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteZoneCommand that = (DeleteZoneCommand) o;
        return Objects.equals(zoneId, that.zoneId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(zoneId);
    }
    
    @Override
    public String toString() {
        return "DeleteZoneCommand{zoneId='" + zoneId + "'}";
    }
}

