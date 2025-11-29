package eco.kosova.infrastructure.persistence.dto;

import java.util.Set;

/**
 * DTO për Zone aggregate.
 */
public class ZoneDTO {
    
    private String id;
    private String name;
    private String status;
    private String municipality;
    private String description;
    private int criticalThreshold;
    
    // Center point
    private double latitude;
    private double longitude;
    
    // Container IDs
    private Set<String> containerIds;
    
    // Timestamps
    private String createdAt;
    private String modifiedAt;
    
    // No-arg constructor
    public ZoneDTO() {
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMunicipality() {
        return municipality;
    }
    
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCriticalThreshold() {
        return criticalThreshold;
    }
    
    public void setCriticalThreshold(int criticalThreshold) {
        this.criticalThreshold = criticalThreshold;
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
    
    public Set<String> getContainerIds() {
        return containerIds;
    }
    
    public void setContainerIds(Set<String> containerIds) {
        this.containerIds = containerIds;
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