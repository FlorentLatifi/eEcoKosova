package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateQytetariRequest {
    
    @Size(min = 2, max = 200, message = "Emri duhet të jetë midis 2 dhe 200 karaktere")
    private String name;
    
    @Size(max = 500, message = "Adresa nuk duhet të jetë më e gjatë se 500 karaktere")
    private String address;
}

