package com.example.nlpEcommerce.service;

import java.util.List;
import java.util.Map;

import com.example.nlpEcommerce.dto.NlpFilterResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class NlpService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.base-url}")
    private String baseUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-3.1-flash-lite-preview}")
    private String modelName;

    public NlpService(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public NlpFilterResult extractFilters(String userQuery) {
        if (userQuery == null || userQuery.isBlank()) {
            return new NlpFilterResult();
        }

        if (apiKey == null || apiKey.isBlank()) {
            // API anahtarı yoksa fallback olarak boş filtre döndür
            return new NlpFilterResult();
        }

        String modelPath = modelName.startsWith("models/") ? modelName : "models/" + modelName;
        String fullUrl = String.format("%s/v1beta/%s:generateContent?key=%s", baseUrl, modelPath, apiKey);

        String prompt = """
                    Sen bir e-ticaret asistanısın. Kullanıcı isteğini analiz et ve SADECE aşağıdaki JSON formatını döndür.
                    Markdown (```json) kullanma. Ekstra açıklama yazma. Sadece saf JSON döndür.

                    JSON formatı:
                    {
                        "category": "String veya null",
                        "minPrice": null,
                        "maxPrice": null,
                        "colors": [],
                        "freeShipping": null,
                        "onDiscount": null,
                        "minRating": null
                    }

                    KURALLAR:
                    1. "category" için SADECE şu listeden birini TAM OLARAK yaz (büyük/küçük harf önemli):
                       Ana: Giyim, Elektronik, Ayakkabi, Canta, Spor
                       Giyim altı: Erkek Giyim, Kadin Giyim, Gomlek, Pantolon, Elbise
                       Elektronik altı: Telefon, Laptop, Kulaklik, Tablet ve Aksesuar, Bilgisayar Bilesenleri, Kamera
                       Ayakkabi altı: Erkek Ayakkabi, Kadin Ayakkabi
                       Canta altı: Sirt Cantasi, El Cantasi
                       Spor altı: Spor Giyim, Spor Ekipmani
                       Hiçbir kategoriye uymuyorsa null yap.
                   ### 2. colors (Renk Çıkarımı ve Normalizasyonu)
                    Sorgudaki renkleri tespit et ve aşağıdaki kurallara göre normalize ederek bir JSON dizisi olarak döndür:

                    - **Eşleşme ve İndirgeme:** - "Al" kelimesini "kirmizi" olarak, "Ak" kelimesini "beyaz" olarak, "Kara" kelimesini "siyah" olarak işle.
                        - Spesifik tonları en yakın ana renge indirge:
                            - [Bordo, Vişne Çürüğü, Nar Çiçeği, Gül Kurusu] -> "kirmizi"
                            - [Lacivert, Turkuaz, Safir, Gök Mavisi] -> "mavi"
                            - [Haki, Zeytin, Fıstık, Çağla] -> "yesil"
                            - [Bej, Ekru, Krem, Somon] -> "beyaz" veya "turuncu" (en yakın hangisiyse)
                            - [toprak, kahverengi, kahve] -> "kahverengi"
                    - **Teknik Kısıtlamalar:**
                        - **Karakter:** Sadece İngilizce karakter kullan (ı->i, ş->s, ğ->g, ü->u, ö->o, ç->c).
                        - **Format:** Çıktı her zaman bir dizi (Array) olmalı. Örn: ["mavi", "kirmizi"].
                        - **Boş Durum:** Renk bulunamazsa veya "al" kelimesi fiil olarak kullanılmış olsa dahi renk listesinde karşılığı varsa `[]` döndür. ASLA null veya undefined döndürme.
                    4. Fiyatlar sadece sayı (para birimi yok): 500, 1500.50
                    5. "freeShipping" ve "onDiscount": belirtilmemişse null, açıkça istenirse true/false.
                    6. "minRating": "yüksek puanlı", "güvenilir" gibi ifadeler varsa 4.0, yoksa null.

                    Kullanıcı cümlesi: "%s"
                """
                .formatted(userQuery);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            String response = restTemplate.postForObject(fullUrl, entity, String.class);
            if (response == null || response.isBlank()) {
                return new NlpFilterResult();
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode textNode = root.path("candidates").get(0)
                    .path("content").path("parts").get(0).path("text");

            String rawText = textNode.asText();
            String jsonText = rawText.replace("```json", "")
                    .replace("```", "")
                    .trim();

            JsonNode parsed = objectMapper.readTree(jsonText);

            NlpFilterResult result = new NlpFilterResult();

            if (parsed.has("category") && !parsed.get("category").isNull()
                    && !parsed.get("category").asText().isBlank()) {
                result.setCategoryKeyword(parsed.get("category").asText().trim());
            }
            if (parsed.has("minPrice") && parsed.get("minPrice").isNumber()) {
                result.setMinPrice(parsed.get("minPrice").decimalValue());
            }
            if (parsed.has("maxPrice") && parsed.get("maxPrice").isNumber()) {
                result.setMaxPrice(parsed.get("maxPrice").decimalValue());
            }
            // "colors" dizisini oku (yeni format) — Gemini Türkçe harf döndürmüş olsa bile
            // normalize ediyoruz
            if (parsed.has("colors") && parsed.get("colors").isArray()) {
                parsed.get("colors").forEach(node -> {
                    String c = normalize(node.asText().trim());
                    if (!c.isBlank())
                        result.addColor(c);
                });
            }
            // "color" tekil string — geriye dönük uyumluluk
            if (result.getColors().isEmpty() && parsed.has("color") && !parsed.get("color").isNull()) {
                String c = normalize(parsed.get("color").asText().trim());
                if (!c.isBlank())
                    result.addColor(c);
            }
            if (parsed.has("freeShipping") && parsed.get("freeShipping").isBoolean()) {
                result.setFreeShipping(parsed.get("freeShipping").asBoolean());
            }
            if (parsed.has("onDiscount") && parsed.get("onDiscount").isBoolean()) {
                result.setOnDiscount(parsed.get("onDiscount").asBoolean());
            }
            if (parsed.has("minRating") && parsed.get("minRating").isNumber()) {
                result.setMinRating(parsed.get("minRating").asDouble());
            }

            result.setIntentSummary("Gemini parse: " + userQuery);
            return result;

        } catch (Exception ex) {
            // Hata durumunda loglayıp fallback için boş filtre döner
            System.err.println("NLP Service parse hatası: " + ex.getMessage());
            return new NlpFilterResult();
        }
    }

    /** Türkçe özel karakterleri ASCII karşılığına dönüştürür. */
    private String normalize(String s) {
        if (s == null)
            return "";
        return s.toLowerCase()
                .replace('ı', 'i').replace('İ', 'i')
                .replace('ğ', 'g').replace('Ğ', 'g')
                .replace('ü', 'u').replace('Ü', 'u')
                .replace('ş', 's').replace('Ş', 's')
                .replace('ö', 'o').replace('Ö', 'o')
                .replace('ç', 'c').replace('Ç', 'c');
    }
}
