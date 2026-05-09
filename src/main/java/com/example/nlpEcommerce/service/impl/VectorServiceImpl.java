package com.example.nlpEcommerce.service.impl;

import com.example.nlpEcommerce.service.*;

import com.example.nlpEcommerce.service.VectorService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class VectorServiceImpl implements VectorService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.base-url}")
    private String baseUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.embedding.model:models/gemini-embedding-001}")
    private String modelName;

    public VectorServiceImpl(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public double[] getEmbedding(String text) {
        if (text == null || text.isBlank() || apiKey == null || apiKey.isBlank()) {
            return new double[0];
        }

        String modelPath = modelName.startsWith("models/") ? modelName : "models/" + modelName;
        String fullUrl = String.format("%s/v1beta/%s:embedContent?key=%s", baseUrl, modelPath, apiKey);

        System.out.println("[VectorService] embed request url=" + fullUrl.replace(apiKey, "<REDACTED>") + " model=" + modelPath + " textLength=" + (text == null ? 0 : text.length()));

        Map<String, Object> body = Map.of(
                "model", modelPath,
                "content", Map.of("parts", List.of(Map.of("text", text)))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(body, headers);

        try {
            String response = restTemplate.postForObject(fullUrl, request, String.class);
            if (response == null || response.isBlank()) {
                return new double[0];
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode values = root.path("embedding").path("values");
            if (!values.isArray()) {
                System.err.println("[VectorService] embedding not array, response=" + response);
                return new double[0];
            }

            double[] vec = new double[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vec[i] = values.get(i).asDouble();
            }
            return vec;

        } catch (Exception e) {
            System.err.println("VectorService embedding error: " + e.getMessage());
            e.printStackTrace();
            return new double[0];
        }
    }

    public double[] parseEmbedding(String json) {
        if (json == null || json.isBlank()) {
            return new double[0];
        }
        try {
            return objectMapper.readValue(json, double[].class);
        } catch (Exception e) {
            return new double[0];
        }
    }

    public String toJson(double[] vector) {
        try {
            return objectMapper.writeValueAsString(vector);
        } catch (Exception e) {
            return "[]";
        }
    }

    public double calculateCosineSimilarity(double[] vectorA, double[] vectorB) {
        if (vectorA == null || vectorB == null || vectorA.length != vectorB.length || vectorA.length == 0) {
            return 0.0;
        }
        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dot += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }
        if (normA == 0.0 || normB == 0.0) return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public String getModelName() {
        return modelName;
    }
}
