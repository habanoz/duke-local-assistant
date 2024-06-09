package com.habanoz.duke.core.graph.parser;

import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.ChatResponseStreamNodeMessage;
import com.habanoz.duke.core.model.NodeFunction;
import com.habanoz.duke.core.model.TextStreamNodeMessage;

public class StringOutParser extends SimpleNode {
    public StringOutParser() {
        super(func());
    }

    private static NodeFunction func() {
        return (input, eventSink) -> input.map(d -> {
            if (d instanceof ChatResponseStreamNodeMessage sm) {
                String content = sm.chatResponse().getResult().getOutput().getContent();
                return new TextStreamNodeMessage(content);
            }
            throw new IllegalArgumentException("Only StreamNodeMessage with chatResponse content is expected");
        });
    }
}
