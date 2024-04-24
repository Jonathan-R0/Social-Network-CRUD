package com.uba.ejercicio.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateUserDto {

    @Email
    private String email;

}
