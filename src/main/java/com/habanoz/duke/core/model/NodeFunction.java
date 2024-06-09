package com.habanoz.duke.core.model;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.BiFunction;

public interface NodeFunction extends BiFunction<Flux<NodeMessage>, Sinks.Many<Event>, Flux<NodeMessage>> {
}
