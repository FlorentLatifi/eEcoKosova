package eco.kosova.application.queries;

/**
 * Query për të marrë statistika për të gjitha zonat.
 */
public final class GetZoneStatisticsQuery {
    
    private static final GetZoneStatisticsQuery INSTANCE = new GetZoneStatisticsQuery();
    
    private GetZoneStatisticsQuery() {
        // Private constructor
    }
    
    public static GetZoneStatisticsQuery getInstance() {
        return INSTANCE;
    }
    
    @Override
    public String toString() {
        return "GetZoneStatisticsQuery{}";
    }
}