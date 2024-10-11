package com.project.review.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponseDto {
    private Long reviewId;
    private Long orderId;
    private Long productId;
    private String comment;
    private int rating;
    private Long userId;
}
