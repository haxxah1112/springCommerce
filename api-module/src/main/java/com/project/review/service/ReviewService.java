package com.project.review.service;

import com.project.common.dto.ApiResponse;
import com.project.review.dto.ReviewRequestDto;

public interface ReviewService {
    ApiResponse createReview(ReviewRequestDto request);
}
