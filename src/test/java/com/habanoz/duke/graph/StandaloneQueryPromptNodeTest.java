package com.habanoz.duke.graph;

import com.habanoz.duke.core.model.Dict;
import com.habanoz.duke.core.model.LoggingEventPublisher;
import com.habanoz.duke.core.model.NodeMessage;
import com.habanoz.duke.core.model.PromptMessage;
import com.habanoz.duke.graph.utils.FormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootTest
class StandaloneQueryPromptNodeTest {

    @Autowired
    private StandaloneQueryPromptNode standaloneQueryPromptNode;

    @Test
    void testQuestion() {
        String question = "what is the capital of Turkey?";
        List<Message> chatHistory = List.of(new UserMessage("Can you search"), new AssistantMessage("Yes"));
        String chatHistoryStr = FormatUtils.formatChatHistory(chatHistory);

        Dict input = Dict.map("question", question, "chatHistory", chatHistory);
        input = FormatUtils.formatChatHistory(input);

        Flux<NodeMessage> stream = standaloneQueryPromptNode.stream(Flux.just(input), new LoggingEventPublisher());

        Assertions.assertEquals(1, stream.count().block());
        Assertions.assertInstanceOf(PromptMessage.class, stream.next().block());
        Assertions.assertTrue(((PromptMessage) stream.next().block()).prompt().getContents().contains(question));
        Assertions.assertTrue(((PromptMessage) stream.next().block()).prompt().getContents().contains(chatHistoryStr));
    }
}