package com.habanoz.duke.graph;

import com.habanoz.duke.core.model.Dict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AnswerGraphTest {
    @Autowired
    AnswerGraph graph;

    @MockBean
    @Qualifier("answerModel")
    private ChatModel answerModel;

    @MockBean
    @Qualifier("deterministicModel")
    private ChatModel deterministicModel;

    @Autowired
    AnswerLlmNode answerLlmNode;

    @Autowired
    DeterministicLlmNode deterministicLlmNode;

    @Test
    void search() {
        AtomicInteger answerIndex = new AtomicInteger();
        AtomicInteger deterministicIndex = new AtomicInteger();

        List<String> answerStrings = List.of("President of Japan is ***");
        List<String> deterministicStrings = List.of("Who is the president of Japan?", "President of Japan");

        Mockito.when(answerModel.call(any(Prompt.class)))
                .thenAnswer(i -> new ChatResponse(
                        Collections.singletonList(new Generation(answerStrings.get(answerIndex.getAndIncrement())))
                ));

        Mockito.when(deterministicModel.call(any(Prompt.class)))
                .thenAnswer(i -> new ChatResponse(
                        Collections.singletonList(new Generation(deterministicStrings.get(deterministicIndex.getAndIncrement())))
                ));

        String query = graph.call(List.of(new UserMessage("Who is the president Japan")));

        Assertions.assertEquals(1, answerIndex.get());
        Assertions.assertEquals(2, deterministicIndex.get());
        Assertions.assertEquals("President of Japan is ***".toLowerCase(), query.toLowerCase());
    }

    @Test
    void searchHistory() {
        AtomicInteger answerIndex = new AtomicInteger();
        AtomicInteger deterministicIndex = new AtomicInteger();

        List<String> answerStrings = List.of("President of Japan is ***");
        List<String> deterministicStrings = List.of("Who is the president of Japan?", "President of Japan");

        Mockito.when(answerModel.call(any(Prompt.class)))
                .thenAnswer(i -> new ChatResponse(
                        Collections.singletonList(new Generation(answerStrings.get(answerIndex.getAndIncrement())))
                ));

        Mockito.when(deterministicModel.call(any(Prompt.class)))
                .thenAnswer(i -> new ChatResponse(
                        Collections.singletonList(new Generation(deterministicStrings.get(deterministicIndex.getAndIncrement())))
                ));

        String query = graph.call(List.of(
                        new UserMessage("Where is the capital Japan"),
                        new UserMessage("Tokio"),
                        new UserMessage("Who is the president")
                )
        );

        Assertions.assertEquals(1, answerIndex.get());
        Assertions.assertEquals(2, deterministicIndex.get());
        Assertions.assertEquals("President of Japan is ***".toLowerCase(), query.toLowerCase());
    }
}