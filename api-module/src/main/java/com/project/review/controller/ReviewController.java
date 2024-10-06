package com.project.review.controller;

import com.project.common.dto.ApiResponse;
import com.project.review.dto.ReviewRequestDto;
import com.project.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse> createReview(@Valid @RequestBody ReviewRequestDto request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

}
