package eco.kosova.domain.models;

import java.time.Instant;

/**
 * Entity: KontrollPanel
 * 
 * Ndërfaqa kryesore për ndërveprim me sistemin.
 * Sipas dokumentit konceptual.
 * 
 * Atribute:
 * - panelID: Identifikues unik i panelë
 * - gjuha: Gjuha e ndërfaqes (shqip, anglisht)
 * - temat: Tema vizuale e ndërfaqes
 * - gjendjaEkranit: Gjendja aktuale e ekranit
 */
public class KontrollPanel {
    
    private final String id;
    private String language; // "shqip", "anglisht"
    private String theme; // "light", "dark"
    private ScreenState screenState;
    private String qytetariId; // Qytetari që përdor panelin
    private Instant createdAt;
    private Instant lastUpdated;
    
    /**
     * Constructor për KontrollPanel
     */
    public KontrollPanel(String id, String qytetariId) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Panel ID cannot be null or blank");
        }
        
        this.id = id;
        this.qytetariId = qytetariId;
        this.language = "shqip"; // Default
        this.theme = "light"; // Default
        this.screenState = ScreenState.HOME;
        this.createdAt = Instant.now();
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Përditëson gjuhën e ndërfaqes
     */
    public void setLanguage(String language) {
        if (language == null || (!language.equals("shqip") && !language.equals("anglisht"))) {
            throw new IllegalArgumentException("Language must be 'shqip' or 'anglisht'");
        }
        this.language = language;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Përditëson temën vizuale
     */
    public void setTheme(String theme) {
        if (theme == null || (!theme.equals("light") && !theme.equals("dark"))) {
            throw new IllegalArgumentException("Theme must be 'light' or 'dark'");
        }
        this.theme = theme;
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Përditëson gjendjen e ekranit
     */
    public void setScreenState(ScreenState screenState) {
        if (screenState == null) {
            throw new IllegalArgumentException("Screen state cannot be null");
        }
        this.screenState = screenState;
        this.lastUpdated = Instant.now();
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public String getTheme() {
        return theme;
    }
    
    public ScreenState getScreenState() {
        return screenState;
    }
    
    public String getQytetariId() {
        return qytetariId;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getLastUpdated() {
        return lastUpdated;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KontrollPanel that = (KontrollPanel) o;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("KontrollPanel{id='%s', language='%s', theme='%s', screenState=%s, qytetariId='%s'}",
                id, language, theme, screenState, qytetariId);
    }
    
    /**
     * Enum për gjendjen e ekranit
     */
    public enum ScreenState {
        HOME("Shtëpi"),
        CONTAINERS("Kontejnerë"),
        ZONES("Zona"),
        REPORTS("Raporte"),
        SETTINGS("Cilësimet");
        
        private final String displayName;
        
        ScreenState(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

