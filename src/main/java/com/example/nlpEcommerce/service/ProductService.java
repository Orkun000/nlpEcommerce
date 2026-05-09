package com.example.nlpEcommerce.service;

import com.example.nlpEcommerce.dto.ProductRequest;
import com.example.nlpEcommerce.dto.ProductResponse;
import com.example.nlpEcommerce.exception.ResourceNotFoundException;
import com.example.nlpEcommerce.model.Category;
import com.example.nlpEcommerce.model.Product;
import com.example.nlpEcommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {
    public Page<ProductResponse> getAllProducts(Pageable pageable);
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);
    public ProductResponse getProductById(Long id);
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable);
    public ProductResponse createProduct(ProductRequest request);
    public ProductResponse updateProduct(Long id, ProductRequest request);
    public void deleteProduct(Long id);
    public Product findProductOrThrow(Long id);
}
