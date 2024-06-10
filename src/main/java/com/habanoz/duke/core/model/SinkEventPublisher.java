package com.habanoz.duke.core.model;

import reactor.core.publisher.Sinks;

public record SinkEventPublisher(Sinks.Many<Event> eventPublisher) implements EventPublisher {

    @Override
    public void publishEvent(Event event) {
        eventPublisher.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public void close() {
        eventPublisher.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
