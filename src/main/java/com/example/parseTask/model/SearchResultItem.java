package com.example.parseTask.model;

import com.example.parseTask.helpers.SearchResultItemDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(using = SearchResultItemDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultItem {
    private String title;
    private String link;
    private SearchResultItemPreview preview;
}
