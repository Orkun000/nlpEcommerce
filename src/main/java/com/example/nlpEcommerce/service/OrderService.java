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

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, UserService userService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = userService.findUserOrThrow(request.getUserId());
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus(Order.OrderStatus.CONFIRMED);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderRequest.OrderItemRequest itemReq : request.getItems()) {
            Product product = productService.findProductOrThrow(itemReq.getProductId());
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(product.getPrice());
            order.getItems().add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }
        order.setTotalAmount(total);
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdWithItems(userId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Siparis", id));
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Siparis", id));
        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        return OrderResponse.from(orderRepository.save(order));
    }
}
