package com.example.nlpEcommerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.nlpEcommerce.dto.NlpFilterResult;
import com.example.nlpEcommerce.dto.NlpSearchRequest;
import com.example.nlpEcommerce.dto.ProductResponse;
import com.example.nlpEcommerce.model.Product;
import com.example.nlpEcommerce.repository.CategoryRepository;
import com.example.nlpEcommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface SearchService {
    public Page<ProductResponse> nlpSearch(NlpSearchRequest request);
    public String indexProductEmbeddings();
    public Page<ProductResponse> keywordSearch(String keyword, int page, int size);
}
