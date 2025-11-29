package eco.kosova.domain.models.valueobjects;

import java.util.Objects;

/**
 * Value Object që përfaqëson nivelin e mbushjes së një kontejneri.
 * Immutable dhe self-validating.
 * 
 * Business Rules:
 * - Niveli duhet të jetë midis 0 dhe 100
 * - 90%+ konsiderohet kritik
 * - 70-89% konsiderohet warning
 * - 0-69% konsiderohet normal
 */
public final class FillLevel {
    
    private final int value;
    
    // Konstantet për nivelet kritike
    private static final int CRITICAL_THRESHOLD = 90;
    private static final int WARNING_THRESHOLD = 70;
    private static final int EMPTY_THRESHOLD = 10;
    
    public FillLevel(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException(
                String.format("Fill level must be between 0-100. Received: %d", value)
            );
        }
        this.value = value;
    }
    
    /**
     * Kontrollon nëse niveli është kritik (90%+)
     */
    public boolean isCritical() {
        return value >= CRITICAL_THRESHOLD;
    }
    
    /**
     * Kontrollon nëse niveli është warning (70-89%)
     */
    public boolean isWarning() {
        return value >= WARNING_THRESHOLD && value < CRITICAL_THRESHOLD;
    }
    
    /**
     * Kontrollon nëse niveli është normal (0-69%)
     */
    public boolean isNormal() {
        return value < WARNING_THRESHOLD;
    }
    
    /**
     * Kontrollon nëse kontejneri është bosh (<=10%)
     */
    public boolean isEmpty() {
        return value <= EMPTY_THRESHOLD;
    }
    
    /**
     * Merr vlerën numerike të nivelit
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Kthen status string bazuar në nivelin e mbushjes
     */
    public String getStatusText() {
        if (isCritical()) return "CRITICAL";
        if (isWarning()) return "WARNING";
        if (isEmpty()) return "EMPTY";
        return "NORMAL";
    }
    
    /**
     * Kthen color code për UI (për frontend)
     */
    public String getColorCode() {
        if (isCritical()) return "#EF4444"; // Red
        if (isWarning()) return "#F59E0B";  // Amber
        if (isEmpty()) return "#6B7280";    // Gray
        return "#10B981";                    // Green
    }
    
    // Value Objects duhet të jenë immutable dhe të implementojnë equals/hashCode
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillLevel fillLevel = (FillLevel) o;
        return value == fillLevel.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return String.format("FillLevel{value=%d%%, status=%s}", value, getStatusText());
    }
}