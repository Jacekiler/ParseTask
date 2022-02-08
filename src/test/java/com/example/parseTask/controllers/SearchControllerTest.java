package com.example.parseTask.controllers;

import com.example.parseTask.services.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.parseTask.utils.Dictionary.MODEL_ATTR_PHRASE;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
class SearchControllerTest {

    private static final String SEARCH_PATH = "/search";
    private static final String SEARCH_RESULT_PATH = "/search/result";
    private static final String PHRASE_VALUE = "faktura";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @BeforeEach
    public void setup(){
        searchService = mock(SearchService.class);
    }

    @Test
    public void shouldBeOkResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SEARCH_PATH))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldBeOkResponseWithParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SEARCH_PATH)
                .param(MODEL_ATTR_PHRASE, PHRASE_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldBeOkResultResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SEARCH_RESULT_PATH))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldBeOkResultResponseWithParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SEARCH_RESULT_PATH)
                .param(MODEL_ATTR_PHRASE, PHRASE_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}