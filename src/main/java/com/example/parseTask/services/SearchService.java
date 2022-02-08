package com.example.parseTask.services;

import com.example.parseTask.model.SearchResult;
import com.example.parseTask.model.SearchResultItem;
import com.example.parseTask.model.SearchResultSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.parseTask.utils.Dictionary.API_RESPONSE_ATTR_ITEMS;
import static com.example.parseTask.utils.Dictionary.API_RESPONSE_ATTR_META;

@Service
public class SearchService {

    private static final Integer DEFAULT_PER_PAGE = 10;
    private static final String API_PATH = "https://pomoc.bluemedia.pl/search-engine/search?query=%s&page=%d&per-page=%d";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SearchService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Optional<SearchResult> search(String phrase, Integer page) throws JsonProcessingException {
        if(phrase == null || phrase.isEmpty()){
            return Optional.empty();
        }
        return extractItemsFromResponse(phrase, page);
    }

    private Optional<SearchResult> extractItemsFromResponse(String phrase, Integer page) throws JsonProcessingException {
        ResponseEntity<String> searchResponse = restTemplate
                .getForEntity(String.format(API_PATH, phrase, page != null ? page : 1, DEFAULT_PER_PAGE), String.class);
        JsonNode node = objectMapper.readTree(searchResponse.getBody());
        Optional<JsonNode> responseItems = Optional.ofNullable(node.get(API_RESPONSE_ATTR_ITEMS));
        Optional<JsonNode> responseSummary = Optional.ofNullable(node.get(API_RESPONSE_ATTR_META));
        if(responseItems.isPresent() && responseSummary.isPresent()){
            List<SearchResultItem> items = objectMapper.readValue(responseItems.get().toString(), new TypeReference<>(){});
            SearchResultSummary summary = objectMapper.readValue(responseSummary.get().toString(), SearchResultSummary.class);
            return Optional.of(new SearchResult(summary, items));
        }
        return Optional.empty();
    }
}
