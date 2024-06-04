package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.vector_store.VectorStoreNode;
import com.habanoz.duke.tool.WebSearchRetriever;
import org.springframework.stereotype.Component;

@Component
public class WebSearchNode extends VectorStoreNode {
    public WebSearchNode(WebSearchRetriever vectorStore) {
        super(vectorStore);
    }
}
