package com.uba.ejercicio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponseDto {

    private String message;

    public ErrorResponseDto(Exception ex) {
        this.message = ex.getMessage();
    }
}
