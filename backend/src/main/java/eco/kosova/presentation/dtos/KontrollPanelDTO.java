package eco.kosova.presentation.dtos;

import java.time.Instant;

/**
 * DTO për KontrollPanel entity.
 */
public class KontrollPanelDTO {
    private String id;
    private String language;
    private String theme;
    private String screenState;
    private String qytetariId;
    private Instant createdAt;
    private Instant lastUpdated;
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public String getScreenState() { return screenState; }
    public void setScreenState(String screenState) { this.screenState = screenState; }
    
    public String getQytetariId() { return qytetariId; }
    public void setQytetariId(String qytetariId) { this.qytetariId = qytetariId; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
}

