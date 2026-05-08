package com.example.nlpEcommerce.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NlpFilterResult {

    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double minRating;
    private List<String> colors = new ArrayList<>();
    private String categoryKeyword;
    private Boolean freeShipping;
    private Boolean onDiscount;
    private String intentSummary;

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public Double getMinRating() { return minRating; }
    public void setMinRating(Double minRating) { this.minRating = minRating; }

    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors != null ? colors : new ArrayList<>(); }

    /** Tek renk eklemek için yardımcı metot. */
    public void addColor(String color) {
        if (color != null && !color.isBlank()) {
            this.colors.add(color.trim().toLowerCase());
        }
    }

    public String getCategoryKeyword() { return categoryKeyword; }
    public void setCategoryKeyword(String categoryKeyword) { this.categoryKeyword = categoryKeyword; }

    public Boolean getFreeShipping() { return freeShipping; }
    public void setFreeShipping(Boolean freeShipping) { this.freeShipping = freeShipping; }

    public Boolean getOnDiscount() { return onDiscount; }
    public void setOnDiscount(Boolean onDiscount) { this.onDiscount = onDiscount; }

    public String getIntentSummary() { return intentSummary; }
    public void setIntentSummary(String intentSummary) { this.intentSummary = intentSummary; }

    @Override
    public String toString() {
        return "NlpFilterResult{" +
               "minPrice=" + minPrice +
               ", maxPrice=" + maxPrice +
               ", minRating=" + minRating +
               ", colors=" + colors +
               ", category='" + categoryKeyword + "'" +
               ", freeShipping=" + freeShipping +
               ", onDiscount=" + onDiscount +
               '}';
    }
}
