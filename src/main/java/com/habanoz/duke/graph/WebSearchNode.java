package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.vector_store.VectorStoreNode;
import com.habanoz.duke.core.model.Event;
import com.habanoz.duke.core.model.NodeMessage;
import com.habanoz.duke.core.model.StringMessage;
import com.habanoz.duke.tool.WebSearchRetriever;

public class WebSearchNode extends VectorStoreNode {
    public WebSearchNode(WebSearchRetriever vectorStore) {
        super(vectorStore);
    }

    public Event getEvent(NodeMessage e) {
        StringMessage sm = (StringMessage) e;
        return new Event("StatusUpdate", "Search started:" + sm.message());
    }

}
