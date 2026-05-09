package com.example.nlpEcommerce.service.impl;

import com.example.nlpEcommerce.service.*;

import com.example.nlpEcommerce.service.CategoryService;

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

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getRootCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id) {
        return CategoryResponse.from(findCategoryOrThrow(id));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(Messages.CATEGORY_DUPLICATE_NAME + request.getName());
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getParentId() != null) {
            category.setParent(findCategoryOrThrow(request.getParentId()));
        }
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategoryOrThrow(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setParent(request.getParentId() != null ? findCategoryOrThrow(request.getParentId()) : null);
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        findCategoryOrThrow(id);
        categoryRepository.deleteById(id);
    }

    public Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori", id));
    }
}
