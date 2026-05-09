package com.example.nlpEcommerce.service;

import com.example.nlpEcommerce.dto.OrderRequest;
import com.example.nlpEcommerce.dto.OrderResponse;
import com.example.nlpEcommerce.exception.ResourceNotFoundException;
import com.example.nlpEcommerce.model.Order;
import com.example.nlpEcommerce.model.OrderItem;
import com.example.nlpEcommerce.model.Product;
import com.example.nlpEcommerce.model.User;
import com.example.nlpEcommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public interface OrderService {
    public OrderResponse createOrder(OrderRequest request);
    public List<OrderResponse> getOrdersByUser(Long userId);
    public OrderResponse getOrderById(Long id);
    public OrderResponse updateStatus(Long id, String status);
}
