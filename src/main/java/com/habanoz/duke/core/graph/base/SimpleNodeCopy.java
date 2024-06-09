package com.habanoz.duke.core.graph.base;

import com.habanoz.duke.core.model.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public class SimpleNodeCopy {
    protected final NodeFunction func;

    public SimpleNodeCopy(NodeFunction func) {
        this.func = func;
    }

    public SimpleNodeCopy chain(SimpleNodeCopy other) {
        return new SimpleNodeCopy((input, eventSink) -> {
            var interimResults = stream(input, eventSink);
            return other.stream(interimResults, eventSink);
        });
    }

    public Flux<NodeMessage> stream(Flux<NodeMessage> stream, Sinks.Many<Event> eventPublisher) {
        stream = convertFlux(stream);

        stream = stream.doOnNext(s -> {
            Event event = getEvent(s);
            if (event != null) {
                eventPublisher.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
            }
        });

        return func.apply(stream, eventPublisher);
    }


    public Event getEvent(NodeMessage e) {
        return null;
    }

    public Flux<NodeMessage> convertFlux(Flux<NodeMessage> flux) {
        return flux;
    }

    protected static BiFunction<StreamNodeMessage, StreamNodeMessage, StreamNodeMessage> reduceStream() {
        return (d1, d2) -> {
            if (d1 instanceof ChatResponseStreamNodeMessage cr1 && d2 instanceof ChatResponseStreamNodeMessage cr2) {
                String content1 = cr1.chatResponse().getResult().getOutput().getContent();
                String content2 = cr2.chatResponse().getResult().getOutput().getContent();

                List<Generation> generations = Collections.singletonList(new Generation(content1 + content2));
                return new ChatResponseStreamNodeMessage(new ChatResponse(generations));
            }

            if (d1 instanceof TextStreamNodeMessage cr1 && d2 instanceof TextStreamNodeMessage cr2) {
                return new TextStreamNodeMessage(cr1.text() + cr2.text());
            }

            throw new IllegalArgumentException("Only StreamNodeMessage accepted");
        };
    }

    protected static Function<StreamNodeMessage, StringMessage> textStreamToText() {
        return streamMessage -> {
            if (streamMessage instanceof TextStreamNodeMessage tm)
                return new StringMessage(tm.text());
            else
                return new StringMessage(((ChatResponseStreamNodeMessage) streamMessage).chatResponse().getResult().getOutput().getContent());
        };
    }
}
