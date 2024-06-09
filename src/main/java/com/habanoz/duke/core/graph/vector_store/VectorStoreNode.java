package com.habanoz.duke.core.graph.vector_store;

import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.*;
import org.springframework.ai.vectorstore.VectorStore;
import reactor.core.publisher.Flux;

public class VectorStoreNode extends SimpleNode {
    public VectorStoreNode(VectorStore vectorStore) {
        super(func(vectorStore));
    }

    private static NodeFunction func(VectorStore vectorStore) {
        return (input, eventSink) -> input
                .map(d -> {
                    if (d instanceof StringMessage sm) {
                        return new ANodeMessage(vectorStore.similaritySearch(sm.message()));
                    }

                    throw new IllegalArgumentException("Only string messages are expected, but got:" + d.getClass());
                });
    }

    @Override
    public Flux<NodeMessage> convertFlux(Flux<NodeMessage> flux) {
        return flux.doOnNext(s -> {
                    if (!(s instanceof TextStreamNodeMessage)) {
                        throw new IllegalArgumentException("Only TextStreamNodeMessage accepted!");
                    }
                }).map(s -> (StreamNodeMessage) s)
                .reduce(reduceStream())
                .map(s -> textStreamToText().apply(s))
                .map(s -> (NodeMessage) s).flux();
    }
}
