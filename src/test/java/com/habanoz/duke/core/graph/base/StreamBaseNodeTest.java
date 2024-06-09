package com.habanoz.duke.core.graph.base;

import com.habanoz.duke.core.model.Dict;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Function;

class StreamBaseNodeTest {

   /* @Test
    void testStream() throws InterruptedException {
        BaseNode node1 = new BaseNode(expand("Who is the PM?"));
        BaseNode node2 = new BaseNode(merge());
        node1.chain(node2).stream(Flux.just(Dict.map("question", "how is everyone?"))).subscribe(s -> System.out.println(s));

        Thread.sleep(15000);
    }

    @Test
    void testBlock() {
        BaseNode node1 = new BaseNode(expand("Who is the PM?"));
        BaseNode node2 = new BaseNode(merge());
        Dict dict = node1.chain(node2).call(Flux.just(Dict.map("question", "how is everyone?")));
        System.out.println(dict);
    }

    private Function<Flux<Dict>, Flux<Dict>> expand(String exp) {
        return s -> s.map(e -> e.extend("extendedQuestion", e.getVal("question") + exp));
    }

    private Function<Flux<Dict>, Flux<Dict>> merge() {
        return s -> Flux.merge(s, Flux.interval(Duration.ofSeconds(2), Duration.ofSeconds(1)).map(i -> Dict.sin(i)).take(10));
    }*/
}