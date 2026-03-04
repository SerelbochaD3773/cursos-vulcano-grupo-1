package com.grupo1.cursosvulcano.model.dto;
import java.time.LocalDateTime;
import org.antlr.v4.runtime.misc.NotNull;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {
 
    private Long id; 

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
    private String username; 

     @NotBlank(message = "El correo electrónico es obligatorio")
     @Size(max = 80, message = "El correo electrónico no puede exceder 80 caracteres")
    private String email; 

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 80, message = "La contraseña debe tener entre 8 y 80 caracteres")    
    private String password; 

    
    private LocalDateTime createdAt; // Fecha y hora en que se registró

    private LocalDateTime updatedAt; // Fecha y 
}
