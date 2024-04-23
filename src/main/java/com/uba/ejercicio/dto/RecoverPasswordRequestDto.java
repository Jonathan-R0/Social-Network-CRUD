package com.uba.ejercicio.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecoverPasswordRequestDto {

    @JsonCreator
    public RecoverPasswordRequestDto(@JsonProperty("password") String password) {
        this.password = password;
    }

    @NotBlank
    @JsonProperty("password")
    private String password;
}
