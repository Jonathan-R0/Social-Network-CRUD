package com.uba.ejercicio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenderCreationDto {
    @NotNull
    private List<String> genders;
}
