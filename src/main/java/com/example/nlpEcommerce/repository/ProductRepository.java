package com.example.nlpEcommerce.repository;

import com.example.nlpEcommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Doğrudan kategori ID eşleşmesi
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Üst kategori seçildiğinde alt kategorilerdeki ürünleri de getirir
    // Örn: Giyim (1) seçilince → Gomlek (4), Pantolon (5), Elbise (6) ürünleri de gelir
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN p.category c " +
           "LEFT JOIN c.parent cp " +
           "LEFT JOIN cp.parent cpp " +
           "WHERE c.id = :categoryId OR cp.id = :categoryId OR cpp.id = :categoryId")
    Page<Product> findByCategoryTree(@Param("categoryId") Long categoryId, Pageable pageable);

    // Keyword arama
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // NLP + dinamik filtreli arama (kategori ağacı dahil, çoklu renk OR desteği)
    // colorsEmpty=true olduğunda renk filtresi devre dışı kalır; false olduğunda LOWER(p.color) IN :colors çalışır.
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN p.category c " +
           "LEFT JOIN c.parent cp " +
           "LEFT JOIN cp.parent cpp " +
           "WHERE " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:minRating IS NULL OR p.rating >= :minRating) AND " +
           "(:categoryId IS NULL OR c.id = :categoryId OR " +
           "  cp.id = :categoryId OR " +
           "  cpp.id = :categoryId) AND " +
           "(:colorsEmpty = true OR LOWER(p.color) IN :colors) AND " +
           "(:freeShipping IS NULL OR p.freeShipping = :freeShipping) AND " +
           "(:onDiscount IS NULL OR p.onDiscount = :onDiscount)")
    Page<Product> findWithFilters(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minRating") Double minRating,
            @Param("categoryId") Long categoryId,
            @Param("colorsEmpty") boolean colorsEmpty,
            @Param("colors") Collection<String> colors,
            @Param("freeShipping") Boolean freeShipping,
            @Param("onDiscount") Boolean onDiscount,
            Pageable pageable
    );
}
