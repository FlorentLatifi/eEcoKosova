package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateContainerRequest {

    @NotBlank(message = "Id e kontejnerit është e detyrueshme")
    @Size(max = 50, message = "Id nuk duhet të jetë më e gjatë se 50 karaktere")
    private String id;

    @NotBlank(message = "ZoneId është i detyrueshëm")
    @Size(max = 50, message = "ZoneId nuk duhet të jetë më i gjatë se 50 karaktere")
    private String zoneId;

    @NotBlank(message = "Lloji i kontejnerit është i detyrueshëm")
    private String type;

    @Min(value = 1, message = "Kapaciteti duhet të jetë > 0")
    private int capacity;

    @DecimalMin(value = "-90.0", message = "Latitude nuk është valid")
    @DecimalMax(value = "90.0", message = "Latitude nuk është valid")
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude nuk është valid")
    @DecimalMax(value = "180.0", message = "Longitude nuk është valid")
    private double longitude;

    @NotBlank(message = "Rruga është e detyrueshme")
    private String street;

    @NotBlank(message = "Qyteti është i detyrueshëm")
    private String city;

    @NotBlank(message = "Komuna është e detyrueshme")
    private String municipality;

    @Size(max = 20, message = "Kodi postar nuk duhet të jetë më i gjatë se 20 karaktere")
    private String postalCode;

    private boolean operational = true;
}


