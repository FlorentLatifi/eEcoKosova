package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateQytetariRequest {
    
    @NotBlank(message = "Id e qytetarit është e detyrueshme")
    @Size(max = 50, message = "Id nuk duhet të jetë më e gjatë se 50 karaktere")
    private String id;
    
    @NotBlank(message = "Emri është i detyrueshëm")
    @Size(min = 2, max = 200, message = "Emri duhet të jetë midis 2 dhe 200 karaktere")
    private String name;
    
    @Size(max = 500, message = "Adresa nuk duhet të jetë më e gjatë se 500 karaktere")
    private String address;
}

