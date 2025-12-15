package eco.kosova.application.queries;

/**
 * Query për të marrë të gjitha kontejnerët kritikë (fill level >= 90%).
 */
public final class GetCriticalContainersQuery {
    
    private static final GetCriticalContainersQuery INSTANCE = new GetCriticalContainersQuery();
    
    private GetCriticalContainersQuery() {
        // Private constructor
    }
    
    public static GetCriticalContainersQuery getInstance() {
        return INSTANCE;
    }
    
    @Override
    public String toString() {
        return "GetCriticalContainersQuery{}";
    }
}