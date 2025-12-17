package eco.kosova.domain.models.valueobjects;

/**
 * Enum që përfaqëson statusin e një zone.
 */
public enum ZoneStatus {
    /**
     * Zona është aktive dhe në përdorim
     */
    ACTIVE("Aktive", "Zone is active and in use"),
    
    /**
     * Zona ka kontejnerë kritikë që duhen mbledhur
     */
    CRITICAL("Kritike", "Zone has critical containers needing collection"),
    
    /**
     * Zona është në mirëmbajtje
     */
    UNDER_MAINTENANCE("Në Mirëmbajtje", "Zone is under maintenance"),
    
    /**
     * Zona është e paaktivizuar përkohësisht
     */
    INACTIVE("Jo-aktive", "Zone is temporarily inactive");
    
    private final String displayName;
    private final String description;
    
    ZoneStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Kontrollon nëse zona ka nevojë për vëmendje
     */
    public boolean needsAttention() {
        return this == CRITICAL || this == UNDER_MAINTENANCE;
    }
    
    /**
     * Merr color code për UI
     */
    public String getColorCode() {
        return switch (this) {
            case ACTIVE -> "#10B981";      // Green
            case CRITICAL -> "#EF4444";    // Red
            case UNDER_MAINTENANCE -> "#F59E0B"; // Amber
            case INACTIVE -> "#6B7280";    // Gray
        };
    }
}