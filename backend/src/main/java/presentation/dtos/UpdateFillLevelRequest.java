package eco.kosova.presentation.dtos;

/**
 * DTO për request të përditësimit të fill level.
 */
public class UpdateFillLevelRequest {
    private int fillLevel;
    
    public UpdateFillLevelRequest() {}
    
    public UpdateFillLevelRequest(int fillLevel) {
        this.fillLevel = fillLevel;
    }
    
    public int getFillLevel() {
        return fillLevel;
    }
    
    public void setFillLevel(int fillLevel) {
        this.fillLevel = fillLevel;
    }
}