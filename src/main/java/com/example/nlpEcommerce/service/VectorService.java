package com.example.nlpEcommerce.service;

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

public interface VectorService {
    public double[] getEmbedding(String text);
    public double[] parseEmbedding(String json);
    public String toJson(double[] vector);
    public double calculateCosineSimilarity(double[] vectorA, double[] vectorB);
    public String getModelName();
}
