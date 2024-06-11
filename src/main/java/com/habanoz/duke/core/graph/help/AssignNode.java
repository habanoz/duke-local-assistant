package com.habanoz.duke.core.graph.help;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AssignNode extends SimpleNode {
    private static final Logger log = LoggerFactory.getLogger(AssignNode.class);

    public AssignNode(String name, BaseNode node) {
        super(func(name, node));
    }

    public AssignNode(String name, Object value) {
        super(func(name, value));
    }

    private static NodeFunction func(String name, BaseNode node) {
        return (input, eventSink) -> {
            Mono<Dict> monoDict = input.next().cast(Dict.class);
            Flux<NodeMessage> resultFlux = node.stream(monoDict.flux().cast(NodeMessage.class), eventSink);

            return resultFlux.reduce((d1, d2) -> {
                        if (d1 instanceof TextStreamNodeMessage tsn1 && d2 instanceof TextStreamNodeMessage tsn2) {
                            return new TextStreamNodeMessage(tsn1.text() + tsn2.text());
                        }

                        throw new IllegalArgumentException("Node must return TextStreamNodeMessage or A single ANodeMessage. " +
                                "For name:" + name + " got:" + d1.getClass().getSimpleName() + "-" + d2.getClass().getSimpleName());
                    }).map(m -> {
                        if (m instanceof TextStreamNodeMessage tsnm) {
                            return new ANodeMessage(tsnm.text());
                        } else if (m instanceof ANodeMessage am) {
                            return am;
                        }

                        throw new IllegalArgumentException("Expected TextStreamNodeMessage or A single ANodeMessage. " +
                                "For name:" + name + " got:" + m.getClass().getSimpleName());
                    }).flatMap(s -> monoDict.map(dict -> dict.extend(name, s.getVal()))).cast(NodeMessage.class).flux();
        };
    }

    private static NodeFunction func(String name, Object value) {
        return (input, eventStream) -> {
            Mono<Dict> dict = input.next().cast(Dict.class);
            return dict.map(m -> m.extend(name, value)).cast(NodeMessage.class).flux();
        };
    }


}
