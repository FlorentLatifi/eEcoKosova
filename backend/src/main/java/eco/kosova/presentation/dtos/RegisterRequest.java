package eco.kosova.presentation.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "Username është i detyrueshëm")
    @Size(min = 3, max = 50, message = "Username duhet të jetë midis 3 dhe 50 karaktere")
    private String username;
    
    @NotBlank(message = "Email është i detyrueshëm")
    @Email(message = "Email nuk është valid")
    private String email;
    
    @NotBlank(message = "Password është i detyrueshëm")
    @Size(min = 6, message = "Password duhet të jetë të paktën 6 karaktere")
    private String password;
    
    @Size(max = 50, message = "Role nuk duhet të jetë më i gjatë se 50 karaktere")
    private String role = "USER";
}

