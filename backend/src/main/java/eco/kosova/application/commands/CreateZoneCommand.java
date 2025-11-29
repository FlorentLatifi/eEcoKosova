package eco.kosova.application.commands;

import java.util.Objects;

/**
 * Command për krijimin e një zone të re.
 */
public final class CreateZoneCommand {
    
    private final String zoneId;
    private final String name;
    private final double latitude;
    private final double longitude;
    private final String municipality;
    private final String description;
    
    public CreateZoneCommand(
            String zoneId,
            String name,
            double latitude,
            double longitude,
            String municipality,
            String description
    ) {
        if (zoneId == null || zoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Zone name cannot be empty");
        }
        if (municipality == null || municipality.isBlank()) {
            throw new IllegalArgumentException("Municipality cannot be empty");
        }
        
        this.zoneId = zoneId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.municipality = municipality;
        this.description = description;
    }
    
    public String getZoneId() {
        return zoneId;
    }
    
    public String getName() {
        return name;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public String getMunicipality() {
        return municipality;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateZoneCommand that = (CreateZoneCommand) o;
        return Objects.equals(zoneId, that.zoneId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(zoneId);
    }
    
    @Override
    public String toString() {
        return String.format(
            "CreateZoneCommand{zoneId='%s', name='%s', municipality='%s'}",
            zoneId, name, municipality
        );
    }
}