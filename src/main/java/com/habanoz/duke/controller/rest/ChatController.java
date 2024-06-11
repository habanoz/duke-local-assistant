package com.habanoz.duke.controller.rest;


import com.habanoz.duke.controller.model.ChatResponseRequest;
import com.habanoz.duke.controller.model.IChatMessage;
import com.habanoz.duke.controller.model.SimpleChatMessage;
import com.habanoz.duke.core.model.*;
import com.habanoz.duke.graph.AnswerGraph;
import com.habanoz.duke.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})
@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private ChatModel answerModel;

    @Autowired
    private AnswerGraph answerGraph;

    @PostMapping(value = "/model-stream", produces = "text/event-stream")
    public Flux<ChatResponse> modelStream(@RequestBody() List<IChatMessage> chatMessages) {
        List<Message> messages = chatMessages.stream().map(this::toMessage).toList();

        return answerModel.stream(new Prompt(messages));
    }


    @PostMapping(value = "/chat")
    public String chat(@RequestBody() List<IChatMessage> chatMessages) {
        List<Message> messages = chatMessages.stream().map(this::toMessage).toList();

        return answerGraph.call(messages);
    }


    @PostMapping(value = "{userId}/chats/{chatId}/messages", produces = "text/event-stream")
    public ResponseEntity<Flux<Event>> getChatResponse(@RequestBody ChatResponseRequest request, @PathVariable("userId") UUID userId, @PathVariable("chatId") UUID chatId) {

        List<Message> messages = request.messages().stream().map(this::toMessage).toList();
        String question = messages.getLast().getContent();

        Sinks.Many<Event> eventSink = Sinks.many().unicast().onBackpressureBuffer();
        EventPublisher eventPublisher = new SinkEventPublisher(eventSink);
        Flux<Event> eventStream = eventSink.asFlux();

        UUID messageId = UUID.randomUUID();
        eventPublisher.publishEvent(new Event("MessageId", messageId));

        Flux<NodeMessage> resultStream = answerGraph.stream(messages, eventPublisher).doOnComplete(eventPublisher::close);

        Flux<Event> eventStreams = eventStream.map(s -> new Event(s.type(), s.content()));

        StringBuilder answerBuilder = new StringBuilder();
        Flux<Event> answerStream = resultStream.map(s -> {
            if (s instanceof ANodeMessage am) {
                return new Event("stream", am.getVal().toString().substring(0, 40));
            } else if (s instanceof TextStreamNodeMessage am) {
                answerBuilder.append(am.text());
                return new Event("stream", am.text());
            } else if (s instanceof ChatResponseStreamNodeMessage am) {
                answerBuilder.append(am.chatResponse().getResult().getOutput().getContent());
                return new Event("stream", am.chatResponse());
            } else if (s instanceof Dict am) {
                return new Event("stream", am.map().keySet());
            }

            throw new IllegalArgumentException("Only ANodeMessage expected but got:" + s.getClass().getSimpleName());
        });

        return ResponseEntity.ok(Flux.merge(eventStreams, answerStream).doOnNext(s -> log.info("Element emitted: {}", s))
                .doOnComplete(() -> chatService.addUtterances(userId, request.userName(), chatId, messageId, question, answerBuilder.toString())));
    }

    private Message toMessage(IChatMessage message) {
        return switch (message.role()) {
            case USER -> new UserMessage(message.content());
            case ASSISTANT -> new AssistantMessage(message.content());
        };
    }

    private Message toMessage(SimpleChatMessage message) {
        return switch (message.role()) {
            case "user" -> new UserMessage(message.content());
            case "assistant" -> new AssistantMessage(message.content());
            default -> throw new IllegalArgumentException("Unknown exception");
        };
    }
}
