package com.example.nlpEcommerce.dto;

import jakarta.validation.constraints.*;

public class ReviewRequest {

    @NotNull
    private Long userId;

    @NotNull @Min(1) @Max(5)
    private Integer rating;

    @Size(max = 1000, message = "Yorum 1000 karakteri gecemez")
    private String comment;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
