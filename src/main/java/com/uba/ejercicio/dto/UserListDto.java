package com.uba.ejercicio.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UserListDto {
    @NotEmpty
    private List<String> emails;
}
