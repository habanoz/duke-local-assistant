package com.habanoz.duke.core.model;

public interface EventPublisher {
    void publishEvent(Event event);
    void close();
}
