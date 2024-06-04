package com.habanoz.duke.core.graph.vector_store;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;
import com.habanoz.duke.core.model.NodeFunction;
import org.springframework.ai.vectorstore.VectorStore;

public class VectorStoreNode extends BaseNode {
    public VectorStoreNode(VectorStore vectorStore) {
        super(func(vectorStore));
    }

    private static NodeFunction func(VectorStore vectorStore) {
        return input -> Dict.sin(vectorStore.similaritySearch(input.str()));
    }
}
