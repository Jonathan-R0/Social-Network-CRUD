package com.uba.ejercicio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    @Email
    private String email;

    @NotBlank
    private String role;

    private Long id;
}
