package com.example.nlpEcommerce.service;

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

public interface ReviewService {
    public List<ReviewResponse> getReviewsByProduct(Long productId);
    public ReviewResponse addReview(Long productId, ReviewRequest request);
    public void deleteReview(Long reviewId, Long userId);
}
