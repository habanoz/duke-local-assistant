package com.habanoz.duke.controller.rest;

import com.habanoz.duke.controller.model.ChatMessage;
import com.habanoz.duke.core.model.*;
import com.habanoz.duke.graph.AnswerGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})

@RestController
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private ChatModel answerModel;

    @Autowired
    private AnswerGraph answerGraph;

    @PostMapping(value = "/model-stream", produces = "text/event-stream")
    public Flux<ChatResponse> modelStream(@RequestBody() List<ChatMessage> chatMessages) {
        List<Message> messages = chatMessages.stream().map(this::toMessage).toList();

        return answerModel.stream(new Prompt(messages));
    }

    @PostMapping(value = "/chat-stream", produces = "text/event-stream")
    public ResponseEntity<Flux<Event>> chatStream(@RequestBody() List<ChatMessage> chatMessages) {
        log.info("new requested");

        List<Message> messages = chatMessages.stream().map(this::toMessage).toList();
        Sinks.Many<Event> eventPublisher = Sinks.many().unicast().onBackpressureBuffer();
        Flux<Event> eventStream = eventPublisher.asFlux();

        log.info("Build answer stream");
        Flux<NodeMessage> resultStream = answerGraph.stream(messages, eventPublisher).doOnComplete(() -> eventPublisher.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST));

        log.info("Build event stream");
        Flux<Event> eventStreams = eventStream.
                map(s -> new Event(s.type(), s.content() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                .doOnNext(s -> System.out.println("StatusUpdate:" + s));

        Flux<Event> answerStream = resultStream.map(s -> {
            if (s instanceof ANodeMessage am) {
                return new Event("stream", am.getVal().toString().substring(0, 40));
            } else if (s instanceof TextStreamNodeMessage am) {
                return new Event("stream", am.text());
            } else if (s instanceof ChatResponseStreamNodeMessage am) {
                return new Event("stream", am.chatResponse());
            } else if (s instanceof Dict am) {
                return new Event("stream", am.map().keySet());
            }

            throw new IllegalArgumentException("Only ANodeMessage expected but got:" + s.getClass().getSimpleName());
        });
        log.info("Return response");
        //return answerStream.mergeWith(eventStreams).map(s -> s);
        return ResponseEntity.ok(Flux.merge(eventStreams, answerStream));
    }

    @PostMapping(value = "/chat-stream1", produces = "text/event-stream")
    public Flux<Event> chatStream1(@RequestBody() List<ChatMessage> chatMessages) {
        List<Message> messages = chatMessages.stream().map(this::toMessage).toList();
        Sinks.Many<Event> eventPublisher = Sinks.many().unicast().onBackpressureBuffer();
        Flux<Event> eventStream = eventPublisher.asFlux();

        Flux<NodeMessage> resultStream = answerGraph.stream(messages, eventPublisher).doOnComplete(() -> eventPublisher.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST));

        Flux<Event> eventStreams = eventStream.
                map(s -> new Event(s.type(), s.content() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                .doOnNext(s -> System.out.println("StatusUpdate:" + s));

        Flux<Event> answerStream = resultStream.map(s -> {
            if (s instanceof ANodeMessage am) {
                return new Event("stream", am.getVal().toString().substring(0, 40));
            } else if (s instanceof TextStreamNodeMessage am) {
                return new Event("stream", am.text());
            } else if (s instanceof ChatResponseStreamNodeMessage am) {
                return new Event("stream", am.chatResponse());
            } else if (s instanceof Dict am) {
                return new Event("stream", am.map().keySet());
            }

            throw new IllegalArgumentException("Only ANodeMessage expected but got:" + s.getClass().getSimpleName());
        });
        //return answerStream.mergeWith(eventStreams).map(s -> s);
        return Flux.merge(eventStreams, answerStream);
    }

    @PostMapping(value = "/chat-stream2", produces = "text/event-stream")
    public SseEmitter chatStream2(@RequestBody() List<ChatMessage> chatMessages) {
        SseEmitter emitter = new SseEmitter();
        try (ExecutorService sseMvcExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            sseMvcExecutor.execute(() -> {
                try {
                    for (int i = 0; i < 50; i++) {
                        SseEmitter.SseEventBuilder event = SseEmitter.event().data(new Event("StatusUpdate", "am.text(): " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
                        emitter.send(event);
                        Thread.sleep(300);
                        System.out.println("Emitted");
                    }
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                }
            });
        }
        return emitter;
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
