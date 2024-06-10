package com.habanoz.duke.core.graph.base;

import com.habanoz.duke.core.model.Event;
import com.habanoz.duke.core.model.EventPublisher;
import com.habanoz.duke.core.model.NodeFunction;
import com.habanoz.duke.core.model.NodeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;


public class SimpleNode extends BaseNode {
    private static final Logger log = LoggerFactory.getLogger(SimpleNode.class);

    protected SimpleNode(NodeFunction func) {
        super(func);
    }

    @Override
    public Flux<NodeMessage> stream(Flux<NodeMessage> stream, EventPublisher eventPublisher) {
        stream = convertFlux(stream);
        stream = stream.doOnNext(s -> {
            Event event = getEvent(s);
            if (event != null) {
                eventPublisher.publishEvent(new Event(event.type(), event.content()));
            }
        });
        return Flux.just(stream).flatMap(str -> func.apply(str, eventPublisher));
    }
}
