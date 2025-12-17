package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank(message = "Username është i detyrueshëm")
    private String username;
    
    @NotBlank(message = "Password është i detyrueshëm")
    private String password;
}

