package eco.kosova.presentation.dtos;

import java.util.List;

/**
 * DTO për API response të rrugëve optimale.
 */
public class RouteResponseDTO {
    private String zoneId;
    private String zoneName;
    private int containerCount;
    private double totalDistanceKm;
    private double estimatedTimeMinutes;
    private int totalCapacityLiters;
    private List<ContainerResponseDTO> containers;
    private String routeType; // "OPTIMAL" ose "PRIORITY_BASED"
    
    // Getters & Setters
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    
    public int getContainerCount() { return containerCount; }
    public void setContainerCount(int containerCount) { this.containerCount = containerCount; }
    
    public double getTotalDistanceKm() { return totalDistanceKm; }
    public void setTotalDistanceKm(double totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }
    
    public double getEstimatedTimeMinutes() { return estimatedTimeMinutes; }
    public void setEstimatedTimeMinutes(double estimatedTimeMinutes) { this.estimatedTimeMinutes = estimatedTimeMinutes; }
    
    public int getTotalCapacityLiters() { return totalCapacityLiters; }
    public void setTotalCapacityLiters(int totalCapacityLiters) { this.totalCapacityLiters = totalCapacityLiters; }
    
    public List<ContainerResponseDTO> getContainers() { return containers; }
    public void setContainers(List<ContainerResponseDTO> containers) { this.containers = containers; }
    
    public String getRouteType() { return routeType; }
    public void setRouteType(String routeType) { this.routeType = routeType; }
}

