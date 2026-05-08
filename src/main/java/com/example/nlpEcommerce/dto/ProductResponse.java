package com.example.nlpEcommerce.dto;

import com.example.nlpEcommerce.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String color;
    private Double rating;
    private Integer reviewCount;
    private Boolean freeShipping;
    private Boolean onDiscount;
    private BigDecimal discountPercentage;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;

    public static ProductResponse from(Product p) {
        ProductResponse r = new ProductResponse();
        r.id = p.getId();
        r.name = p.getName();
        r.description = p.getDescription();
        r.price = p.getPrice();
        r.stock = p.getStock();
        r.imageUrl = p.getImageUrl();
        r.color = p.getColor();
        r.rating = p.getRating();
        r.reviewCount = p.getReviewCount();
        r.freeShipping = p.getFreeShipping();
        r.onDiscount = p.getOnDiscount();
        r.discountPercentage = p.getDiscountPercentage();
        r.categoryId = p.getCategory().getId();
        r.categoryName = p.getCategory().getName();
        r.createdAt = p.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public String getColor() { return color; }
    public Double getRating() { return rating; }
    public Integer getReviewCount() { return reviewCount; }
    public Boolean getFreeShipping() { return freeShipping; }
    public Boolean getOnDiscount() { return onDiscount; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public Long getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
