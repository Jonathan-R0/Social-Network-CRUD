package com.uba.ejercicio.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FollowResponseDto {
    private List<String> follows;
}
