package com.habanoz.duke.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record WebSearchRetriever() implements VectorStore {
    private static final Logger log = LoggerFactory.getLogger(WebSearchRetriever.class);

    @Override
    public List<Document> similaritySearch(SearchRequest request) {

        List<DDGS.SearchResult> searchResults = new DDGS(request.getTopK()).search(request.getQuery());
        try {
            return new ConcurrentURLFetcher().fetch(searchResults.stream().map(DDGS.SearchResult::href).toList());
        } catch (InterruptedException e) {
            log.warn("Interrupted", e);
            return Collections.emptyList();
        }
    }


    @Override
    public void add(List<Document> documents) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public Optional<Boolean> delete(List<String> idList) {
        return Optional.empty();
    }
}