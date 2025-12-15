package eco.kosova.application.commands;

import java.util.Objects;

/**
 * Command për përditësimin e një kontejneri ekzistues.
 */
public final class UpdateContainerCommand {
    
    private final String id;
    private final String zoneId;
    private final String type;
    private final int capacity;
    private final double latitude;
    private final double longitude;
    private final String street;
    private final String city;
    private final String municipality;
    private final String postalCode;
    private final boolean operational;
    private final Integer fillLevel; // Optional
    
    public UpdateContainerCommand(
            String id,
            String zoneId,
            String type,
            Integer capacity,
            Double latitude,
            Double longitude,
            String street,
            String city,
            String municipality,
            String postalCode,
            Boolean operational,
            Integer fillLevel
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be empty");
        }
        
        this.id = id;
        this.zoneId = zoneId;
        this.type = type;
        this.capacity = capacity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.city = city;
        this.municipality = municipality;
        this.postalCode = postalCode;
        this.operational = operational;
        this.fillLevel = fillLevel;
    }
    
    // Getters
    public String getId() { return id; }
    public String getZoneId() { return zoneId; }
    public String getType() { return type; }
    public Integer getCapacity() { return capacity; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getMunicipality() { return municipality; }
    public String getPostalCode() { return postalCode; }
    public Boolean getOperational() { return operational; }
    public Integer getFillLevel() { return fillLevel; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateContainerCommand that = (UpdateContainerCommand) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

