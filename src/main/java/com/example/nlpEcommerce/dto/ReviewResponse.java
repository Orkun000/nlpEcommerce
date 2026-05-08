package com.example.nlpEcommerce.dto;

import com.example.nlpEcommerce.model.Review;

import java.time.LocalDateTime;

public class ReviewResponse {

    private Long id;
    private Long userId;
    private String userFullName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public static ReviewResponse from(Review r) {
        ReviewResponse res = new ReviewResponse();
        res.id = r.getId();
        res.userId = r.getUser().getId();
        res.userFullName = r.getUser().getFirstName() + " " + r.getUser().getLastName();
        res.rating = r.getRating();
        res.comment = r.getComment();
        res.createdAt = r.getCreatedAt();
        return res;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserFullName() { return userFullName; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
