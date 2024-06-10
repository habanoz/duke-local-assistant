package com.habanoz.duke.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record LoggingEventPublisher() implements EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(LoggingEventPublisher.class);

    @Override
    public void publishEvent(Event event) {
        log.info("New event: {}", event);
    }

    @Override
    public void close() {
        log.info("EVent published closed!");
    }
}
