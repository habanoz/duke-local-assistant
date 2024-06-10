package com.habanoz.duke.core.graph.help;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;
import com.habanoz.duke.core.model.LoggingEventPublisher;
import com.habanoz.duke.core.model.NodeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

class AssignNodeTest {

    @Test
    void testValueAssignment() {
        AssignNode assignNode = new AssignNode("value", 123);
        Flux<NodeMessage> stream = assignNode.stream(Flux.just(Dict.map("name", "Joe")), new LoggingEventPublisher());

        Assertions.assertEquals(1, stream.count().block());
        Assertions.assertInstanceOf(Dict.class, stream.next().block());
        Dict nodeMessage = (Dict) stream.next().block();
        Assertions.assertEquals(123, nodeMessage.getInt("value"));
    }

    @Test
    void testValueGraph() {
        BaseNode actionGraph = new ActionNode(dict -> dict.extend("counter", dict.getInt("target"))).chain(new DictToANodeMessageNode("target"));
        AssignNode assignNode = new AssignNode("value", actionGraph);
        Flux<NodeMessage> stream = assignNode.stream(Flux.just(Dict.map("name", "Joe", "target", 77)), new LoggingEventPublisher());

        Assertions.assertEquals(1, stream.count().block());
        Assertions.assertInstanceOf(Dict.class, stream.next().block());

        Dict nodeMessage = (Dict) stream.next().block();
        Assertions.assertEquals(77, nodeMessage.getInt("value"));
    }
}