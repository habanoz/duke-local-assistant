package com.habanoz.duke.core.model;

import reactor.core.publisher.Flux;

import java.util.function.BiFunction;

public interface NodeFunction extends BiFunction<Flux<NodeMessage>, EventPublisher, Flux<NodeMessage>> {
}
