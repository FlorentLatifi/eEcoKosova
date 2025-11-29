package eco.kosova.domain.models.valueobjects;

/**
 * Enum që përfaqëson statusin operacional të një kontejneri.
 */
public enum ContainerStatus {
    /**
     * Kontejneri është operativ dhe i gatshëm për përdorim
     */
    OPERATIONAL("Operativ", "Container is operational and ready for use"),
    
    /**
     * Kontejneri është i mbushur dhe duhet të mblidhet
     */
    FULL("I Mbushur", "Container is full and needs collection"),
    
    /**
     * Kontejneri është caktuar për mbledhje
     */
    SCHEDULED_FOR_COLLECTION("Caktuar për Mbledhje", "Container is scheduled for collection"),
    
    /**
     * Kontejneri është në mirëmbajtje
     */
    UNDER_MAINTENANCE("Në Mirëmbajtje", "Container is under maintenance"),
    
    /**
     * Kontejneri është jashtë funksionit
     */
    OUT_OF_SERVICE("Jashtë Funksionit", "Container is out of service"),
    
    /**
     * Kontejneri është dëmtuar
     */
    DAMAGED("I Dëmtuar", "Container is damaged");
    
    private final String displayName;
    private final String description;
    
    ContainerStatus(String displayName, String description) {
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
     * Kontrollon nëse kontejneri mund të përdoret për mbledhje
     */
    public boolean isAvailableForCollection() {
        return this == OPERATIONAL || this == FULL || this == SCHEDULED_FOR_COLLECTION;
    }
    
    /**
     * Kontrollon nëse kontejneri ka nevojë për vëmendje të menjëhershme
     */
    public boolean needsAttention() {
        return this == FULL || this == UNDER_MAINTENANCE || this == DAMAGED;
    }
    
    /**
     * Merr color code bazuar në status (për UI)
     */
    public String getColorCode() {
        return switch (this) {
            case OPERATIONAL -> "#10B981";              // Green
            case FULL -> "#EF4444";                     // Red
            case SCHEDULED_FOR_COLLECTION -> "#3B82F6"; // Blue
            case UNDER_MAINTENANCE -> "#F59E0B";        // Amber
            case OUT_OF_SERVICE -> "#6B7280";           // Gray
            case DAMAGED -> "#DC2626";                  // Dark Red
        };
    }
}