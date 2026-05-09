package com.example.nlpEcommerce.service.impl;

import com.example.nlpEcommerce.service.*;

import com.example.nlpEcommerce.service.ProductService;

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

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponse::from);
    }

    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryTree(categoryId, pageable).map(ProductResponse::from);
    }

    public ProductResponse getProductById(Long id) {
        return ProductResponse.from(findProductOrThrow(id));
    }

    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, pageable).map(ProductResponse::from);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryService.findCategoryOrThrow(request.getCategoryId());
        Product product = new Product();
        applyRequest(product, request, category);
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProductOrThrow(id);
        Category category = categoryService.findCategoryOrThrow(request.getCategoryId());
        applyRequest(product, request, category);
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        findProductOrThrow(id);
        productRepository.deleteById(id);
    }

    private void applyRequest(Product product, ProductRequest req, Category category) {
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock() != null ? req.getStock() : 0);
        product.setImageUrl(req.getImageUrl());
        product.setColor(req.getColor());
        product.setRating(req.getRating() != null ? req.getRating() : 0.0);
        product.setFreeShipping(req.getFreeShipping() != null ? req.getFreeShipping() : false);
        product.setOnDiscount(req.getOnDiscount() != null ? req.getOnDiscount() : false);
        product.setDiscountPercentage(req.getDiscountPercentage());
        product.setCategory(category);
    }

    public Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Urun", id));
    }
}
