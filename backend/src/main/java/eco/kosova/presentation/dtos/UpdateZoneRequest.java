package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateZoneRequest {
    
    @Size(min = 2, max = 200, message = "Emri duhet të jetë midis 2 dhe 200 karaktere")
    private String name;
    
    @DecimalMin(value = "-90.0", message = "Latitude nuk është valid")
    @DecimalMax(value = "90.0", message = "Latitude nuk është valid")
    private Double latitude;
    
    @DecimalMin(value = "-180.0", message = "Longitude nuk është valid")
    @DecimalMax(value = "180.0", message = "Longitude nuk është valid")
    private Double longitude;
    
    @Size(max = 100, message = "Municipality nuk duhet të jetë më e gjatë se 100 karaktere")
    private String municipality;
    
    @Size(max = 1000, message = "Description nuk duhet të jetë më e gjatë se 1000 karaktere")
    private String description;
}

