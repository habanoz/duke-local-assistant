package com.habanoz.duke.core.graph.base;

import com.habanoz.duke.core.model.Event;
import com.habanoz.duke.core.model.NodeFunction;
import com.habanoz.duke.core.model.NodeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class SimpleNode extends BaseNode {
    private static final Logger log = LoggerFactory.getLogger(SimpleNode.class);
    public SimpleNode(NodeFunction func) {
        super(func);
    }

    @Override
    public Flux<NodeMessage> stream(Flux<NodeMessage> stream, Sinks.Many<Event> eventPublisher) {
        log.info("Stream enter {}", getClass().getSimpleName());
        stream = convertFlux(stream);//.doOnNext(n-> System.out.println("stream : OnNext: "+getClass().getSimpleName()+": "+n.toString().substring(0,Math.min(n.toString().length(), 150))));
        log.info("Convert flux {}", getClass().getSimpleName());
        stream = stream.doOnNext(s -> {
            Event event = getEvent(s);
            if (event != null) {
                System.out.println("Emit Status Update:"+event.content());
                eventPublisher.emitNext(new Event(event.type(), event.content()+" "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))), Sinks.EmitFailureHandler.FAIL_FAST);
            }
        });
        log.info("Emit event {}", getClass().getSimpleName());
        Flux<NodeMessage> apply = func.apply(stream, eventPublisher);
        log.info("Function applied {}", getClass().getSimpleName());
        return apply;
    }
}
