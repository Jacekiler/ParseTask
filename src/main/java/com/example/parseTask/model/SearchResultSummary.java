package com.example.parseTask.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResultSummary {

    private String totalCount;
    private String pageCount;
    private String currentPage;
    private String perPage;
}
