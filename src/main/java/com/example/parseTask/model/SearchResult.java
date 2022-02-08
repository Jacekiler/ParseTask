package com.example.parseTask.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SearchResult {
    private SearchResultSummary summary;
    private List<SearchResultItem> items;
}
