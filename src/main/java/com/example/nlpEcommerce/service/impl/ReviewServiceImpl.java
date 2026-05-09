package com.example.nlpEcommerce.service.impl;

import com.example.nlpEcommerce.service.*;

import com.example.nlpEcommerce.service.ReviewService;

import com.example.nlpEcommerce.constant.Messages;
import com.example.nlpEcommerce.dto.ReviewRequest;
import com.example.nlpEcommerce.dto.ReviewResponse;
import com.example.nlpEcommerce.exception.DuplicateResourceException;
import com.example.nlpEcommerce.exception.ResourceNotFoundException;
import com.example.nlpEcommerce.model.Product;
import com.example.nlpEcommerce.model.Review;
import com.example.nlpEcommerce.model.User;
import com.example.nlpEcommerce.repository.ProductRepository;
import com.example.nlpEcommerce.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository,
            UserService userService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponse addReview(Long productId, ReviewRequest request) {
        if (reviewRepository.existsByProductIdAndUserId(productId, request.getUserId())) {
            throw new DuplicateResourceException(Messages.PRODUCT_ALREADY_REVIEWED);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Urun", productId));
        User user = userService.findUserOrThrow(request.getUserId());

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        ReviewResponse response = ReviewResponse.from(reviewRepository.save(review));

        // Update product's avg rating and review count
        Double avg = reviewRepository.avgRatingByProductId(productId);
        long count = reviewRepository.countByProductId(productId);
        if (avg != null) {
            product.setRating(Math.round(avg * 10.0) / 10.0);
        }
        product.setReviewCount((int) count);
        productRepository.save(product);

        return response;
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Yorum", reviewId));
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(Messages.NO_PERMISSION_DELETE_REVIEW);
        }
        reviewRepository.delete(review);
    }
}
