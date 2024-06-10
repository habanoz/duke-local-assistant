package com.habanoz.duke.graph;

import com.habanoz.duke.core.model.Dict;
import com.habanoz.duke.core.model.LoggingEventPublisher;
import com.habanoz.duke.core.model.NodeMessage;
import com.habanoz.duke.core.model.PromptMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class SearchQueryPromptNodeTest {
    @Autowired
    private SearchQueryPromptNode promptNode;

    @Test
    void testQuestion() {
        String question = "what is the capital of Turkey?";
        Dict input = Dict.map("question", question);

        Flux<NodeMessage> stream = promptNode.stream(Flux.just(input), new LoggingEventPublisher());

        Assertions.assertEquals(1, stream.count().block());
        Assertions.assertInstanceOf(PromptMessage.class, stream.next().block());
        Assertions.assertTrue(((PromptMessage) stream.next().block()).prompt().getContents().contains(question));
    }
}