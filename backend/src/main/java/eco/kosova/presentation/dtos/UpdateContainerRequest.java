package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateContainerRequest {
    
    @Size(max = 50, message = "ZoneId nuk duhet të jetë më i gjatë se 50 karaktere")
    private String zoneId;
    
    @Size(max = 50, message = "Type nuk duhet të jetë më i gjatë se 50 karaktere")
    private String type;
    
    @Min(value = 1, message = "Kapaciteti duhet të jetë > 0")
    private Integer capacity;
    
    @DecimalMin(value = "-90.0", message = "Latitude nuk është valid")
    @DecimalMax(value = "90.0", message = "Latitude nuk është valid")
    private Double latitude;
    
    @DecimalMin(value = "-180.0", message = "Longitude nuk është valid")
    @DecimalMax(value = "180.0", message = "Longitude nuk është valid")
    private Double longitude;
    
    @Size(max = 200, message = "Street nuk duhet të jetë më e gjatë se 200 karaktere")
    private String street;
    
    @Size(max = 100, message = "City nuk duhet të jetë më e gjatë se 100 karaktere")
    private String city;
    
    @Size(max = 100, message = "Municipality nuk duhet të jetë më e gjatë se 100 karaktere")
    private String municipality;
    
    @Size(max = 20, message = "PostalCode nuk duhet të jetë më i gjatë se 20 karaktere")
    private String postalCode;
    
    private Boolean operational;
    
    @Min(value = 0, message = "Fill level nuk mund të jetë negativ")
    @Max(value = 100, message = "Fill level nuk mund të jetë më i madh se 100%")
    private Integer fillLevel;
}

