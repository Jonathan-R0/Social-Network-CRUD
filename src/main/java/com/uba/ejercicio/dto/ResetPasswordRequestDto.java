package com.uba.ejercicio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    @NotBlank
    @Email
    private String email;
}
