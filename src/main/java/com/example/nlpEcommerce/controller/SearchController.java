package com.example.nlpEcommerce.controller;

import com.example.nlpEcommerce.dto.ApiResponse;
import com.example.nlpEcommerce.dto.NlpSearchRequest;
import com.example.nlpEcommerce.dto.ProductResponse;
import com.example.nlpEcommerce.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search", description = "Urun arama - keyword ve NLP destekli")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/nlp/index-embeddings")
    @Operation(summary = "Ürün embeddinglerini indexle")
    public ResponseEntity<ApiResponse<String>> indexEmbeddings() {
        String result = searchService.indexProductEmbeddings();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/nlp")
    @Operation(
        summary = "Dogal dil ile urun ara",
        description = "Kullanicinin dogal dil sorgusunu (ornek: '500 TL alti kirmizi gomlekler') " +
                      "yapisal filtrelere donusturup sonuclari dondurur. Sprint 3'te LLM API entegrasyonu tamamlanacak."
    )
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> nlpSearch(@Valid @RequestBody NlpSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(searchService.nlpSearch(request)));
    }

    @GetMapping
    @Operation(summary = "Keyword ile urun ara")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> keywordSearch(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(searchService.keywordSearch(q, page, size)));
    }
}
