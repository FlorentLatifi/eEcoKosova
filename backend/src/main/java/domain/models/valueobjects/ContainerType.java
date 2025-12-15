package eco.kosova.domain.models.valueobjects;

/**
 * Enum që përfaqëson llojet e ndryshme të kontejnerëve të mbeturinave.
 */
public enum ContainerType {
    /**
     * Kontejner për mbetje plastike
     */
    PLASTIC("Plastikë", "Plastic waste", "#FCD34D", 1000),
    
    /**
     * Kontejner për mbetje xhami
     */
    GLASS("Xham", "Glass waste", "#60A5FA", 800),
    
    /**
     * Kontejner për mbetje metalike
     */
    METAL("Metal", "Metal waste", "#9CA3AF", 600),
    
    /**
     * Kontejner për mbetje organike
     */
    ORGANIC("Organik", "Organic waste", "#34D399", 1200),
    
    /**
     * Kontejner për mbetje të përgjithshme (të përziera)
     */
    GENERAL("I Përgjithshëm", "General/mixed waste", "#64748B", 1500),
    
    /**
     * Kontejner për letra dhe karton
     */
    PAPER("Letër/Karton", "Paper and cardboard", "#A78BFA", 800);
    
    private final String displayName;
    private final String description;
    private final String colorCode;
    private final int defaultCapacity; // në litra
    
    ContainerType(String displayName, String description, String colorCode, int defaultCapacity) {
        this.displayName = displayName;
        this.description = description;
        this.colorCode = colorCode;
        this.defaultCapacity = defaultCapacity;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public int getDefaultCapacity() {
        return defaultCapacity;
    }
    
    /**
     * Kontrollon nëse lloji është riciklues
     */
    public boolean isRecyclable() {
        return this == PLASTIC || this == GLASS || this == METAL || this == PAPER;
    }
    
    /**
     * Merr ikonën për UI (emri i ikonës nga Lucide React)
     */
    public String getIconName() {
        return switch (this) {
            case PLASTIC -> "bottle";
            case GLASS -> "wine";
            case METAL -> "box";
            case ORGANIC -> "leaf";
            case GENERAL -> "trash-2";
            case PAPER -> "file-text";
        };
    }
}