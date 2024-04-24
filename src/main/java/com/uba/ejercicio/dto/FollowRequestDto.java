package com.uba.ejercicio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowRequestDto {

    public FollowRequestDto(@JsonProperty("follow") Long follow) {
        this.follow = follow;
    }

    @Min(1)
    @JsonProperty("follow")
    private Long follow;
}
