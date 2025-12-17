package eco.kosova.presentation.dtos;

/**
 * DTO për API response të kontejnerëve.
 */
public class ContainerResponseDTO {
    private String id;
    private String zoneId;
    private String type;
    private int fillLevel;
    private String status;
    private int capacity;
    private boolean operational;
    private double latitude;
    private double longitude;
    private String address;
    private boolean needsCollection;
    
    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public int getFillLevel() { return fillLevel; }
    public void setFillLevel(int fillLevel) { this.fillLevel = fillLevel; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public boolean isOperational() { return operational; }
    public void setOperational(boolean operational) { this.operational = operational; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public boolean isNeedsCollection() { return needsCollection; }
    public void setNeedsCollection(boolean needsCollection) { this.needsCollection = needsCollection; }
}