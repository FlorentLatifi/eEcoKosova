package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * DTO për request të përditësimit të fill level.
 */
@Data
public class UpdateFillLevelRequest {
    
    @Min(value = 0, message = "Fill level nuk mund të jetë negativ")
    @Max(value = 100, message = "Fill level nuk mund të jetë më i madh se 100%")
    private int fillLevel;
}