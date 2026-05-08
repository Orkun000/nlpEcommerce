package com.example.nlpEcommerce.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "Urun adi bos olamaz")
    @Size(max = 200)
    private String name;

    private String description;

    @NotNull(message = "Fiyat zorunludur")
    @DecimalMin(value = "0.0", inclusive = false, message = "Fiyat 0'dan buyuk olmalidir")
    private BigDecimal price;

    @Min(value = 0, message = "Stok negatif olamaz")
    private Integer stock = 0;

    private String imageUrl;
    private String color;

    @DecimalMin("0.0") @DecimalMax("5.0")
    private Double rating = 0.0;

    private Boolean freeShipping = false;
    private Boolean onDiscount = false;

    @DecimalMin("0.0") @DecimalMax("100.0")
    private BigDecimal discountPercentage;

    @NotNull(message = "Kategori ID zorunludur")
    private Long categoryId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Boolean getFreeShipping() { return freeShipping; }
    public void setFreeShipping(Boolean freeShipping) { this.freeShipping = freeShipping; }

    public Boolean getOnDiscount() { return onDiscount; }
    public void setOnDiscount(Boolean onDiscount) { this.onDiscount = onDiscount; }

    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
