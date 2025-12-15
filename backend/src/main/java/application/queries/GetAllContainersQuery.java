package eco.kosova.application.queries;

/**
 * Query për të marrë të gjitha kontejnerët.
 * Nuk ka parametra - thjesht lexon të gjitha të dhënat.
 */
public final class GetAllContainersQuery {
    
    // Singleton pattern - kjo query nuk ka parametra
    private static final GetAllContainersQuery INSTANCE = new GetAllContainersQuery();
    
    private GetAllContainersQuery() {
        // Private constructor
    }
    
    public static GetAllContainersQuery getInstance() {
        return INSTANCE;
    }
    
    @Override
    public String toString() {
        return "GetAllContainersQuery{}";
    }
}