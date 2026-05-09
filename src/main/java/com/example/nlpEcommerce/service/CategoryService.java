package com.example.nlpEcommerce.service;

import com.example.nlpEcommerce.constant.Messages;
import com.example.nlpEcommerce.dto.CategoryRequest;
import com.example.nlpEcommerce.dto.CategoryResponse;
import com.example.nlpEcommerce.exception.DuplicateResourceException;
import com.example.nlpEcommerce.exception.ResourceNotFoundException;
import com.example.nlpEcommerce.model.Category;
import com.example.nlpEcommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

public interface CategoryService {
    public List<CategoryResponse> getAllCategories();
    public List<CategoryResponse> getRootCategories();
    public CategoryResponse getCategoryById(Long id);
    public CategoryResponse createCategory(CategoryRequest request);
    public CategoryResponse updateCategory(Long id, CategoryRequest request);
    public void deleteCategory(Long id);
    public Category findCategoryOrThrow(Long id);
}
