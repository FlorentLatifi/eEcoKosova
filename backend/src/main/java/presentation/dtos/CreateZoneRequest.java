package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateZoneRequest {

    @NotBlank(message = "ZoneId është i detyrueshëm")
    @Size(max = 50, message = "ZoneId nuk duhet të jetë më i gjatë se 50 karaktere")
    private String zoneId;

    @NotBlank(message = "Emri i zonës është i detyrueshëm")
    @Size(max = 200, message = "Emri nuk duhet të jetë më i gjatë se 200 karaktere")
    private String name;

    @DecimalMin(value = "-90.0", message = "Latitude nuk është valid")
    @DecimalMax(value = "90.0", message = "Latitude nuk është valid")
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude nuk është valid")
    @DecimalMax(value = "180.0", message = "Longitude nuk është valid")
    private double longitude;

    @NotBlank(message = "Komuna është e detyrueshme")
    @Size(max = 100, message = "Komuna nuk duhet të jetë më e gjatë se 100 karaktere")
    private String municipality;

    @Size(max = 1000, message = "Përshkrimi nuk duhet të jetë më i gjatë se 1000 karaktere")
    private String description;
}


