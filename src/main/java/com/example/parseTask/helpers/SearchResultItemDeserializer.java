package com.example.parseTask.helpers;

import com.example.parseTask.model.SearchResultItem;
import com.example.parseTask.model.SearchResultItemPreview;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

import static com.example.parseTask.utils.Dictionary.*;

public class SearchResultItemDeserializer extends StdDeserializer<SearchResultItem> {

    public SearchResultItemDeserializer() {
        this(null);
    }

    public SearchResultItemDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public SearchResultItem deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String title = node.get(API_RESPONSE_ATTR_TITLE).asText();
        String link = node.get(API_RESPONSE_ATTR_LINK).asText();
        String rawPreview = node.get(API_RESPONSE_ATTR_PREVIEW).asText();
        SearchResultItemPreview preview = new ObjectMapper().readValue(rawPreview, SearchResultItemPreview.class);
        return new SearchResultItem(title, link, preview);
    }
}
