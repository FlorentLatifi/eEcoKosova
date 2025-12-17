package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CreateCikliRequest {
    
    @NotBlank(message = "Id e ciklit është e detyrueshme")
    @Size(max = 50, message = "Id nuk duhet të jetë më e gjatë se 50 karaktere")
    private String id;
    
    @NotNull(message = "Koha e planifikimit është e detyrueshme")
    private LocalDateTime scheduleTime;
    
    @Min(value = 1, message = "Kapaciteti maksimal duhet të jetë > 0")
    private int maxCapacity;
    
    @NotBlank(message = "ZoneId është i detyrueshëm")
    @Size(max = 50, message = "ZoneId nuk duhet të jetë më i gjatë se 50 karaktere")
    private String zoneId;
    
    @NotEmpty(message = "Ditët e mbledhjes nuk mund të jenë bosh")
    private Set<DayOfWeek> collectionDays;
    
    @Size(max = 50, message = "KamioniId nuk duhet të jetë më i gjatë se 50 karaktere")
    private String kamioniId;
}

