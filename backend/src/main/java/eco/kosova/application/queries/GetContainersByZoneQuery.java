package eco.kosova.application.queries;

import java.util.Objects;

/**
 * Query për të marrë të gjitha kontejnerët e një zone.
 */
public final class GetContainersByZoneQuery {
    
    private final String zoneId;
    
    public GetContainersByZoneQuery(String zoneId) {
        if (zoneId == null || zoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be empty");
        }
        
        this.zoneId = zoneId;
    }
    
    public String getZoneId() {
        return zoneId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetContainersByZoneQuery that = (GetContainersByZoneQuery) o;
        return Objects.equals(zoneId, that.zoneId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(zoneId);
    }
    
    @Override
    public String toString() {
        return String.format("GetContainersByZoneQuery{zoneId='%s'}", zoneId);
    }
}