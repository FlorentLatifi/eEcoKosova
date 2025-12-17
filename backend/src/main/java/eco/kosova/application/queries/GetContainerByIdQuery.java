package eco.kosova.application.queries;

import java.util.Objects;

/**
 * Query për të marrë një kontejner sipas ID-së.
 * 
 * CQRS Pattern: Queries përfaqësojnë intent për të lexuar të dhëna.
 * 
 * Queries janë:
 * - Immutable
 * - Read-only intent
 * - No side effects
 */
public final class GetContainerByIdQuery {
    
    private final String containerId;
    
    public GetContainerByIdQuery(String containerId) {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be empty");
        }
        
        this.containerId = containerId;
    }
    
    public String getContainerId() {
        return containerId;
    }
    
    /**
     * Factory method për krijimin e query-së.
     */
    public static GetContainerByIdQuery of(String containerId) {
        return new GetContainerByIdQuery(containerId);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetContainerByIdQuery that = (GetContainerByIdQuery) o;
        return Objects.equals(containerId, that.containerId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(containerId);
    }
    
    @Override
    public String toString() {
        return String.format("GetContainerByIdQuery{containerId='%s'}", containerId);
    }
}