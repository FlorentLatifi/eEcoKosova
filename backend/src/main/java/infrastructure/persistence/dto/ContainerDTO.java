package eco.kosova.infrastructure.persistence.dto;

/**
 * DTO (Data Transfer Object) për serialization/deserialization të Kontenier.
 * 
 * Përdoret për të konvertuar domain objects në JSON dhe anasjelltas.
 * DTO-të janë struktura të thjeshta pa business logic.
 */
public class ContainerDTO {
    
    private String id;
    private String zoneId;
    private String type;
    private int fillLevel;
    private String status;
    private int capacity;
    private boolean operational;
    
    // Location
    private double latitude;
    private double longitude;
    
    // Address
    private String street;
    private String city;
    private String municipality;
    private String postalCode;
    
    // Timestamps (ISO format strings)
    private String lastEmptied;
    private String lastUpdated;
    private String createdAt;
    private String modifiedAt;
    
    // No-arg constructor (required by Gson)
    public ContainerDTO() {
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getZoneId() {
        return zoneId;
    }
    
    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public int getFillLevel() {
        return fillLevel;
    }
    
    public void setFillLevel(int fillLevel) {
        this.fillLevel = fillLevel;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public boolean isOperational() {
        return operational;
    }
    
    public void setOperational(boolean operational) {
        this.operational = operational;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getMunicipality() {
        return municipality;
    }
    
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getLastEmptied() {
        return lastEmptied;
    }
    
    public void setLastEmptied(String lastEmptied) {
        this.lastEmptied = lastEmptied;
    }
    
    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getModifiedAt() {
        return modifiedAt;
    }
    
    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}