package com.habanoz.duke.core.graph.llm;

import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.ChatResponseStreamNodeMessage;
import com.habanoz.duke.core.model.NodeFunction;
import com.habanoz.duke.core.model.PromptMessage;
import org.springframework.ai.chat.model.ChatModel;

public class LlmNode extends SimpleNode {
    public LlmNode(ChatModel chatModel) {
        super(callLlm(chatModel));
    }

    private static NodeFunction callLlm(ChatModel chatModel) {
        return (input, eventSink) -> input.flatMap(d -> {
            if (d instanceof PromptMessage pm) {
                // System.out.println("LlmNode: Prompt: >>>>>>>>>>>\n"+pm.prompt()+"\n<<<<<<<<<<<<<<<<<<<");
                return chatModel.stream(pm.prompt());
            }

            throw new IllegalArgumentException("Only prompt message allowed");
        }).map(ChatResponseStreamNodeMessage::new).map(s->{
            // System.out.println("LlmNode: ChatResponse: "+s.chatResponse().getResult().getOutput().getContent());
            return s;
        });
    }
}
