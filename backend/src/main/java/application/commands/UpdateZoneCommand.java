package eco.kosova.application.commands;

import java.util.Objects;

/**
 * Command për përditësimin e një zone ekzistuese.
 */
public final class UpdateZoneCommand {
    
    private final String zoneId;
    private final String name;
    private final Double latitude;
    private final Double longitude;
    private final String municipality;
    private final String description;
    
    public UpdateZoneCommand(
            String zoneId,
            String name,
            Double latitude,
            Double longitude,
            String municipality,
            String description
    ) {
        if (zoneId == null || zoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be empty");
        }
        
        this.zoneId = zoneId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.municipality = municipality;
        this.description = description;
    }
    
    // Getters
    public String getZoneId() { return zoneId; }
    public String getName() { return name; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getMunicipality() { return municipality; }
    public String getDescription() { return description; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateZoneCommand that = (UpdateZoneCommand) o;
        return Objects.equals(zoneId, that.zoneId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(zoneId);
    }
}

