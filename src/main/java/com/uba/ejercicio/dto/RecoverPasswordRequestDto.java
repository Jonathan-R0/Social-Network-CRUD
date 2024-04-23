package com.uba.ejercicio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecoverPasswordRequestDto {
    @NotBlank
    private String password;
}
