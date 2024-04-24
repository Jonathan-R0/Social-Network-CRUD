package com.uba.ejercicio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnfollowRequestDto {

    public UnfollowRequestDto(@JsonProperty("unfollow") Long unfollow) {
        this.unfollow = unfollow;
    }

    @Min(1)
    @JsonProperty("unfollow")
    private Long unfollow;
}
