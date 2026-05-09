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

public interface NlpService {
    public NlpFilterResult extractFilters(String userQuery);
}
