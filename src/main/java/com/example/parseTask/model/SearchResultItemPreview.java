package com.example.parseTask.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultItemPreview {
    private String text;
    private SearchResultItemPreviewLink[] breadcrumbs;
}
