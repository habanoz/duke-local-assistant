package com.habanoz.duke.core.graph.help;

import com.habanoz.duke.core.model.Dict;
import com.habanoz.duke.core.model.LoggingEventPublisher;
import com.habanoz.duke.core.model.NodeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

class ActionNodeTest {

    @Test
    void testDictConstructor() {
        ActionNode actionNode = new ActionNode(dict -> dict.extend("counter", dict.getInt("target")));
        Flux<NodeMessage> stream = actionNode.stream(Flux.just(Dict.map("target", 3)), new LoggingEventPublisher());

        Assertions.assertEquals(1, stream.count().block());
        Assertions.assertInstanceOf(Dict.class, stream.next().block());
        Dict nodeMessage = (Dict) stream.next().block();
        Assertions.assertEquals(3, nodeMessage.getInt("target"));
    }

}