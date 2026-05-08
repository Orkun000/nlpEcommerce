package com.example.nlpEcommerce.dto;

import com.example.nlpEcommerce.model.Order;
import com.example.nlpEcommerce.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private Long userId;
    private String userFullName;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private List<ItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderResponse from(Order o) {
        OrderResponse r = new OrderResponse();
        r.id = o.getId();
        r.userId = o.getUser().getId();
        r.userFullName = o.getUser().getFirstName() + " " + o.getUser().getLastName();
        r.status = o.getStatus().name();
        r.totalAmount = o.getTotalAmount();
        r.shippingAddress = o.getShippingAddress();
        r.paymentMethod = o.getPaymentMethod();
        r.createdAt = o.getCreatedAt();
        r.updatedAt = o.getUpdatedAt();
        r.items = o.getItems().stream().map(ItemDto::from).collect(Collectors.toList());
        return r;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserFullName() { return userFullName; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getShippingAddress() { return shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public List<ItemDto> getItems() { return items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static class ItemDto {
        private Long productId;
        private String productName;
        private String categoryName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;

        public static ItemDto from(OrderItem i) {
            ItemDto d = new ItemDto();
            d.productId = i.getProduct().getId();
            d.productName = i.getProduct().getName();
            d.categoryName = i.getProduct().getCategory() != null ? i.getProduct().getCategory().getName() : null;
            d.quantity = i.getQuantity();
            d.unitPrice = i.getUnitPrice();
            d.subtotal = i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()));
            return d;
        }

        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public String getCategoryName() { return categoryName; }
        public Integer getQuantity() { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public BigDecimal getSubtotal() { return subtotal; }
    }
}
