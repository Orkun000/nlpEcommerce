package com.example.nlpEcommerce.controller;

import com.example.nlpEcommerce.constant.Messages;
import com.example.nlpEcommerce.dto.ApiResponse;
import com.example.nlpEcommerce.dto.ReviewRequest;
import com.example.nlpEcommerce.dto.ReviewResponse;
import com.example.nlpEcommerce.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/reviews")
@Tag(name = "Reviews", description = "Urun yorumlari")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(summary = "Urune ait yorumlari listele")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByProduct(productId)));
    }

    @PostMapping
    @Operation(summary = "Yorum ekle")
    public ResponseEntity<ApiResponse<ReviewResponse>> addReview(
            @PathVariable Long productId, @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(Messages.REVIEW_ADDED, reviewService.addReview(productId, request)));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Yorumu sil")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @RequestParam Long userId) {
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(ApiResponse.success(Messages.REVIEW_DELETED, null));
    }
}
