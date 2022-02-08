package com.example.parseTask.controllers;

import com.example.parseTask.model.SearchResult;
import com.example.parseTask.model.SearchResultItem;
import com.example.parseTask.services.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import static com.example.parseTask.utils.Dictionary.*;

@Controller
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /*
    Loads empty search page
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(value = MODEL_ATTR_PHRASE, required = false) String phrase,
            @RequestParam(value = MODEL_ATTR_PAGE, required = false) Integer page, Model model) {
        model.addAttribute(MODEL_ATTR_PHRASE, phrase);
        model.addAttribute(MODEL_ATTR_PAGE, page);
        return "search";
    }

    /*
    Loads search result items into list on search page
     */
    @GetMapping("/search/result")
    public String getResult(@RequestParam(value = MODEL_ATTR_PHRASE, required = false) String phrase,
                              @RequestParam(value = MODEL_ATTR_PAGE, required = false) Integer page, Model model) throws InterruptedException, JsonProcessingException {
        Optional<SearchResult> result = searchService.search(phrase, page);
        model.addAttribute(MODEL_ATTR_PHRASE, phrase);
        model.addAttribute(MODEL_ATTR_PAGE, page);
        result.ifPresent(res ->{
            model.addAttribute(MODEL_ATTR_ITEMS, res.getItems());
            model.addAttribute(MODEL_ATTR_PAGES, res.getSummary().getPageCount());
        });
        return "search_result :: result_list";
    }

}
