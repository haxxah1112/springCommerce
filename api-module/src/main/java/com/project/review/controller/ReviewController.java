package com.project.review.controller;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.ReviewSearchDto;
import com.project.domain.users.UserRole;
import com.project.review.dto.ReviewRequestDto;
import com.project.review.dto.ReviewResponseDto;
import com.project.review.service.ReviewService;
import com.project.security.CheckRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @CheckRole(accessLevel = UserRole.BUYER)
    public ResponseEntity<ApiResponse> createReview(@Valid @RequestBody ReviewRequestDto request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @GetMapping("/product")
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> getReviewsByProductId(@ModelAttribute ReviewSearchDto reviewSearchDto) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByProductId(reviewSearchDto);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

}
