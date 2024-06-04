package com.habanoz.duke.controller.rest;

import com.habanoz.duke.controller.model.ChatMessage;
import com.habanoz.duke.graph.AnswerGraph;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private ChatModel answerModel;

    @Autowired
    private AnswerGraph answerGraph;

    @PostMapping(value = "/chat-stream", produces = "text/event-stream")
    public Flux<ChatResponse> chatStream(@RequestBody() List<ChatMessage> chatMessages) {
        List<Message> messages = chatMessages.stream().map(this::toMessage).toList();

        return answerModel.stream(new Prompt(messages));
    }

    @PostMapping(value = "/chat")
    public String chat(@RequestBody() List<ChatMessage> chatMessages) {
        List<Message> messages = chatMessages.stream().map(this::toMessage).toList();

        return answerGraph.call(messages);
    }

    private Message toMessage(ChatMessage message) {
        return switch (message.role()) {
            case "user" -> new UserMessage(message.content());
            case "assistant" -> new AssistantMessage(message.content());
            default -> throw new IllegalArgumentException("Unknown exception");
        };
    }
}
