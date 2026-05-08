package com.example.nlpEcommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NlpSearchRequest {

    @NotBlank(message = "Arama sorgusu bos olamaz")
    @Size(max = 500, message = "Sorgu 500 karakterden uzun olamaz")
    private String query;

    private int page = 0;
    private int size = 20;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}
