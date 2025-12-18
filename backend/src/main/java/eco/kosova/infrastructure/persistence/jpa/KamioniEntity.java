package eco.kosova.infrastructure.persistence.jpa;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kamionet")
public class KamioniEntity {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private Instant installationDate;

    @Column(nullable = false)
    private Instant lastUpdated;

    @Column(nullable = false, length = 20, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, length = 50)
    private String operatorId;

    @Column(length = 50)
    private String currentRouteId;

    @ElementCollection
    @CollectionTable(name = "kamioni_assigned_containers", joinColumns = @JoinColumn(name = "kamioni_id"))
    @Column(name = "container_id")
    private List<String> assignedContainerIds = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt;

    public KamioniEntity() {
    }

    // Getters and setters

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

    public Instant getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Instant installationDate) {
        this.installationDate = installationDate;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCurrentRouteId() {
        return currentRouteId;
    }

    public void setCurrentRouteId(String currentRouteId) {
        this.currentRouteId = currentRouteId;
    }

    public List<String> getAssignedContainerIds() {
        return assignedContainerIds;
    }

    public void setAssignedContainerIds(List<String> assignedContainerIds) {
        this.assignedContainerIds = assignedContainerIds != null ? assignedContainerIds : new ArrayList<>();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

