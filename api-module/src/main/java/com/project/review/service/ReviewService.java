package com.project.review.service;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.ReviewSearchDto;
import com.project.review.dto.ReviewRequestDto;
import com.project.review.dto.ReviewResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    ApiResponse createReview(ReviewRequestDto request);

    Page<ReviewResponseDto> getReviewsByProductId(ReviewSearchDto reviewSearchDto);
}
