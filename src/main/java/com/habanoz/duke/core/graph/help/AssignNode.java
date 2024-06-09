package com.habanoz.duke.core.graph.help;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Optional;

public class AssignNode extends SimpleNode {
    public AssignNode(String name, BaseNode node) {
        super(func(name, node));
    }

    public AssignNode(String name, Object value) {
        super(func(name, value));
    }

    private static NodeFunction func2(String name, BaseNode node) {
        return (input, eventSink) -> input.map(d -> {
            if (d instanceof Dict dict) {
                Flux<NodeMessage> stream = node.stream(Flux.just(d), eventSink).map(s -> s);
                NodeMessage nm = stream.blockFirst();
                if (nm instanceof TextStreamNodeMessage am) {
                    TextStreamNodeMessage tsm = stream.reduce((d1, d2) -> new TextStreamNodeMessage(((TextStreamNodeMessage) d1).text() + ((TextStreamNodeMessage) d2).text())).map(s -> (TextStreamNodeMessage) s).block();
                    eventSink.emitNext(new Event("StatusUpdate", name + ":" + tsm.text()), Sinks.EmitFailureHandler.FAIL_FAST);
                    return dict.extend(name, tsm.text());
                } else if (nm instanceof ANodeMessage am) {
                    eventSink.emitNext(new Event("StatusUpdate", name + ":" + am.getVal()), Sinks.EmitFailureHandler.FAIL_FAST);
                    return dict.extend(name, am.getVal());
                }

                throw new IllegalArgumentException("Node must return TextStreamNodeMessage or ANodeMessage. For name:" + name + " returned:" + nm.getClass());
            }

            throw new IllegalArgumentException("NodeFunction only works with Dict elements");
        });
    }

    private static NodeFunction func(String name, BaseNode node) {
        return (input, eventSink) -> {
            Mono<Dict> monoDict = input.next().cast(Dict.class);
            Dict dict = monoDict.blockOptional().orElseThrow(()->new IllegalArgumentException("Unable to obtain the dict"));

            Flux<NodeMessage> resultFlux = node.stream(Flux.just(dict), eventSink);

            Optional<ANodeMessage> oResultMessage = resultFlux.reduce((d1, d2) -> {
                if (d1 instanceof TextStreamNodeMessage tsn1 && d2 instanceof TextStreamNodeMessage tsn2) {
                    return new TextStreamNodeMessage(tsn1.text() + tsn2.text());
                }

                throw new IllegalArgumentException("Node must return TextStreamNodeMessage or A single ANodeMessage. " +
                        "For name:" + name + " got:" + d1.getClass().getSimpleName() + "-" + d2.getClass().getSimpleName());
            }).map(m -> {
                if (m instanceof TextStreamNodeMessage tsnm) {
                    // eventSink.emitNext(new Event("StatusUpdate", name + ":" + tsnm.text()), Sinks.EmitFailureHandler.FAIL_FAST);
                    return new ANodeMessage(tsnm.text());
                } else if (m instanceof ANodeMessage am) {
                    // eventSink.emitNext(new Event("StatusUpdate", name + ":" + am.getVal()), Sinks.EmitFailureHandler.FAIL_FAST);
                    return am;
                }

                throw new IllegalArgumentException("Expected TextStreamNodeMessage or A single ANodeMessage. " +
                        "For name:" + name + " got:" + m.getClass().getSimpleName());
            }).blockOptional();

            return Flux.just(dict.extend(name, oResultMessage.map(ANodeMessage::getVal).orElse(null)));
        };
    }

    private static NodeFunction func(String name, Object value) {
        return (input, eventStream) -> {
            Mono<Dict> dict = input.next().cast(Dict.class);
            return dict.map(m -> m.extend(name, value)).cast(NodeMessage.class).flux();
        };
    }


}
