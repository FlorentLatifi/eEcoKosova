package eco.kosova.presentation.dtos;

import java.time.Instant;

/**
 * DTO për Kamioni entity.
 */
public class KamioniDTO {
    private String id;
    private String name;
    private String licensePlate;
    private int capacity;
    private String operatorId;
    private String status;
    private double latitude;
    private double longitude;
    private String currentRouteId;
    private int assignedContainerCount;
    private Instant installationDate;
    private Instant lastUpdated;
    private boolean available;
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public String getCurrentRouteId() { return currentRouteId; }
    public void setCurrentRouteId(String currentRouteId) { this.currentRouteId = currentRouteId; }
    
    public int getAssignedContainerCount() { return assignedContainerCount; }
    public void setAssignedContainerCount(int assignedContainerCount) { this.assignedContainerCount = assignedContainerCount; }
    
    public Instant getInstallationDate() { return installationDate; }
    public void setInstallationDate(Instant installationDate) { this.installationDate = installationDate; }
    
    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

