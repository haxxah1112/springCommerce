package com.project.review.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    @NotNull
    private Long orderId;

    @NotBlank
    @Size(max = 500)
    private String comment;

    @Min(value = 1)
    @Max(value = 5)
    private int rating;

    @NotNull
    private Long productId;
}
