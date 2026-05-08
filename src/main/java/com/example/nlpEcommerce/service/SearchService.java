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


@Service
@Transactional(readOnly = true)
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final NlpService nlpService;
    private final VectorService vectorService;

    public SearchService(ProductRepository productRepository, CategoryRepository categoryRepository, NlpService nlpService, VectorService vectorService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.nlpService = nlpService;
        this.vectorService = vectorService;
    }

    public Page<ProductResponse> nlpSearch(NlpSearchRequest request) {
        log.info("NLP arama sorgusu alindi: '{}'", request.getQuery());

        NlpFilterResult filters = nlpService.extractFilters(request.getQuery());
        if (filters == null || !hasAnyFilter(filters)) {
            filters = parseMockFilters(request.getQuery());
        }

        if (filters.getIntentSummary() == null || filters.getIntentSummary().isBlank()) {
            filters.setIntentSummary("Mock parse: " + request.getQuery());
        }

        log.info("Cikarilan filtreler: {}", filters);

        Long categoryId = resolveCategoryId(filters.getCategoryKeyword());

        // if (!hasAnyFilter(filters) && categoryId == null) {
        //     log.info("Yapisal filtre bulunamadi, keyword aramaya fallback yapiliyor.");
        //     return keywordSearch(request.getQuery(), request.getPage(), request.getSize());
        // }

        List<String> colors = filters.getColors();
        boolean colorsEmpty = colors == null || colors.isEmpty();

        // ilk olarak gelen yapılandırılmış filtrelere göre ürünler
        Page<Product> filteredPage = productRepository.findWithFilters(
                filters.getMinPrice(), filters.getMaxPrice(), filters.getMinRating(),
                categoryId, colorsEmpty, colorsEmpty ? List.of() : colors,
                filters.getFreeShipping(), filters.getOnDiscount(),
                PageRequest.of(0, 1000)
        );

        List<Product> filteredProducts = new ArrayList<>(filteredPage.getContent());

        // Vektörel benzerliğe göre sırala ve düşük skorlu ürünleri kes
        final double SIMILARITY_THRESHOLD = 0.60;
        double[] queryEmbedding = vectorService.getEmbedding(request.getQuery());
        if (queryEmbedding.length > 0 && !filteredProducts.isEmpty()) {
            filteredProducts.sort(Comparator.comparingDouble((Product p) -> {
                String embJson = p.getEmbedding();
                if (embJson == null || embJson.isBlank()) return 0.0;
                double[] productEmbedding = vectorService.parseEmbedding(embJson);
                return vectorService.calculateCosineSimilarity(queryEmbedding, productEmbedding);
            }).reversed());

            // Eşik değerinin altında kalan alakasız ürünleri listeden çıkar
            filteredProducts.removeIf(p -> {
                String embJson = p.getEmbedding();
                if (embJson == null || embJson.isBlank()) return true;
                double[] productEmbedding = vectorService.parseEmbedding(embJson);
                double score = vectorService.calculateCosineSimilarity(queryEmbedding, productEmbedding);
                log.debug("Urun '{}' similarity skoru: {}", p.getName(), score);
                return score < SIMILARITY_THRESHOLD;
            });

            log.info("Threshold ({}) sonrasi kalan urun sayisi: {}", SIMILARITY_THRESHOLD, filteredProducts.size());
        }

        // sayfalama
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredProducts.size());
        List<ProductResponse> pageContent = new ArrayList<>();
        if (start <= end) {
            for (Product p : filteredProducts.subList(start, end)) {
                pageContent.add(ProductResponse.from(p));
            }
        }

        return new PageImpl<>(pageContent, pageable, filteredProducts.size());
    }

    @Transactional
    public String indexProductEmbeddings() {
        List<Product> products = productRepository.findAll();
        int count = 0;

        for (Product product : products) {
            if (product.getEmbedding() == null || product.getEmbedding().isBlank()) {
                String textToEmbed = (product.getName() != null ? product.getName() : "") + " " +
                        (product.getDescription() != null ? product.getDescription() : "");
                System.out.println("[SearchService] index embedding for productId=" + product.getId() + " name=" + product.getName());

                double[] embedding = vectorService.getEmbedding(textToEmbed.trim());
                if (embedding.length > 0) {
                    product.setEmbedding(vectorService.toJson(embedding));
                    productRepository.save(product);
                    count++;
                } else {
                    System.err.println("[SearchService] embedding empty for productId=" + product.getId() + " name=" + product.getName());
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Embedding indexleme islemi kesildi, productId={}", product.getId());
                    break;
                }
            } else {
                System.out.println("[SearchService] product already has embedding productId=" + product.getId());
            }
        }

        String result = count + " ürünün embedding'i oluşturuldu.";
        System.out.println("[SearchService] indexProductEmbeddings result=" + result);
        return result;
    }

    public Page<ProductResponse> keywordSearch(String keyword, int page, int size) {
        return productRepository.searchByKeyword(keyword, PageRequest.of(page, size))
                .map(ProductResponse::from);
    }

    /**
     * Sprint 3'te bu metot LLM API çağrısıyla değiştirilecek.
     * Şu an basit kural tabanlı parse mantığı kullanılmaktadır.
     */
    private NlpFilterResult parseMockFilters(String query) {
        NlpFilterResult result = new NlpFilterResult();
        String lq = query.toLowerCase();
        String lqNorm = normalize(lq); // Türkçe harfler ASCII'ye

        Matcher maxM = Pattern.compile("(\\d+)\\s*(tl|lira)\\s*(alt|den ucuz|den az)").matcher(lqNorm);
        if (maxM.find()) result.setMaxPrice(new BigDecimal(maxM.group(1)));

        Matcher minM = Pattern.compile("(\\d+)\\s*(tl|lira)\\s*(uzer|fazla|den pahali)").matcher(lqNorm);
        if (minM.find()) result.setMinPrice(new BigDecimal(minM.group(1)));

        Matcher ratingM = Pattern.compile("([1-5])\\s*yildiz").matcher(lqNorm);
        if (ratingM.find()) {
            result.setMinRating(Double.parseDouble(ratingM.group(1)));
        } else if (lqNorm.contains("yuksek puanli") || lqNorm.contains("iyi puanli")) {
            result.setMinRating(4.0);
        }

        // Normalize edilmiş sorguda tüm renkleri tara (kırmızı → kirmizi eşleşir)
        for (String color : new String[]{"kirmizi", "mavi", "yesil", "siyah", "beyaz", "sari", "mor", "turuncu", "pembe", "gri"}) {
            if (lqNorm.contains(color)) result.addColor(color);
        }

        if (lqNorm.contains("ucretsiz kargo") || lqNorm.contains("bedava kargo")) result.setFreeShipping(true);
        if (lqNorm.contains("indirim") || lqNorm.contains("kampanya")) result.setOnDiscount(true);

        for (String cat : new String[]{"gomlek", "ayakkabi", "pantolon", "elbise", "telefon", "laptop", "kulaklik"}) {
            if (lqNorm.contains(cat)) { result.setCategoryKeyword(cat); break; }
        }

        result.setIntentSummary("Mock parse: " + query);
        return result;
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase()
                .replace('ı', 'i').replace('İ', 'i')
                .replace('ğ', 'g').replace('Ğ', 'g')
                .replace('ü', 'u').replace('Ü', 'u')
                .replace('ş', 's').replace('Ş', 's')
                .replace('ö', 'o').replace('Ö', 'o')
                .replace('ç', 'c').replace('Ç', 'c');
    }

    private Long resolveCategoryId(String keyword) {
        if (keyword == null) return null;
        return categoryRepository.findByName(keyword).map(c -> c.getId()).orElse(null);
    }

    private boolean hasAnyFilter(NlpFilterResult filters) {
        return filters.getMinPrice() != null
                || filters.getMaxPrice() != null
                || filters.getMinRating() != null
                || !filters.getColors().isEmpty()
                || (filters.getCategoryKeyword() != null && !filters.getCategoryKeyword().isBlank())
                || filters.getFreeShipping() != null
                || filters.getOnDiscount() != null;
    }
}
