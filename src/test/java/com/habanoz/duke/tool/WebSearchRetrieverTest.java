package com.habanoz.duke.tool;

import com.habanoz.duke.core.model.LoggingEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.List;

class WebSearchRetrieverTest {
    @Test
    void testSearch() {
        WebSearchRetriever webSearchRetriever = new WebSearchRetriever(new LoggingEventPublisher());
        List<Document> results = webSearchRetriever.similaritySearch(SearchRequest.query("Capital of France").withTopK(3));

        Assertions.assertEquals(3, results.size());
        for (Document result : results) {
            Assertions.assertTrue(result.getContent().toLowerCase().contains("paris"));
        }

    }
}