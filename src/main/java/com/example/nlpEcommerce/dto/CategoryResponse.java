package com.example.nlpEcommerce.dto;


import com.example.nlpEcommerce.model.Category;

public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private String parentName;

    public static CategoryResponse from(Category c) {
        CategoryResponse r = new CategoryResponse();
        r.id = c.getId();
        r.name = c.getName();
        r.description = c.getDescription();
        if (c.getParent() != null) {
            r.parentId = c.getParent().getId();
            r.parentName = c.getParent().getName();
        }
        return r;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Long getParentId() { return parentId; }
    public String getParentName() { return parentName; }
}
