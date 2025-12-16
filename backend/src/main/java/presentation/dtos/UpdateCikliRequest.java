package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UpdateCikliRequest {
    
    private LocalDateTime scheduleTime;
    
    @Min(value = 1, message = "Kapaciteti maksimal duhet të jetë > 0")
    private Integer maxCapacity;
    
    private Set<DayOfWeek> collectionDays;
    
    @Size(max = 50, message = "KamioniId nuk duhet të jetë më i gjatë se 50 karaktere")
    private String kamioniId;
}

