package eco.kosova.application.commands;

import java.util.Objects;

/**
 * Command për krijimin e një kontejneri të ri.
 */
public final class CreateContainerCommand {
    
    private final String id;
    private final String zoneId;
    private final String type; // ContainerType display name
    private final int capacity;
    private final double latitude;
    private final double longitude;
    private final String street;
    private final String city;
    private final String municipality;
    private final String postalCode;
    private final boolean operational;
    
    public CreateContainerCommand(
            String id,
            String zoneId,
            String type,
            int capacity,
            double latitude,
            double longitude,
            String street,
            String city,
            String municipality,
            String postalCode,
            boolean operational
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be empty");
        }
        if (zoneId == null || zoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be empty");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Container type cannot be empty");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        if (municipality == null || municipality.isBlank()) {
            throw new IllegalArgumentException("Municipality cannot be empty");
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
        this.postalCode = postalCode != null ? postalCode : "";
        this.operational = operational;
    }
    
    // Getters
    public String getId() { return id; }
    public String getZoneId() { return zoneId; }
    public String getType() { return type; }
    public int getCapacity() { return capacity; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getMunicipality() { return municipality; }
    public String getPostalCode() { return postalCode; }
    public boolean isOperational() { return operational; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateContainerCommand that = (CreateContainerCommand) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

