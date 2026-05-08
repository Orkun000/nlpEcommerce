package com.example.nlpEcommerce.controller;

import com.example.nlpEcommerce.constant.Messages;
import com.example.nlpEcommerce.dto.ApiResponse;
import com.example.nlpEcommerce.dto.OrderRequest;
import com.example.nlpEcommerce.dto.OrderResponse;
import com.example.nlpEcommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Siparis yonetimi")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Yeni siparis olustur")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(Messages.ORDER_CREATED, orderService.createOrder(request)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Kullanicinin siparislerini listele")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrdersByUser(userId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Siparis detayi")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(id)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Siparis durumunu guncelle (Admin)")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {
        return ResponseEntity
                .ok(ApiResponse.success(Messages.ORDER_STATUS_UPDATED, orderService.updateStatus(id, status)));
    }
}
