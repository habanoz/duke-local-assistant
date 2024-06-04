package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DeterministicLlmNode extends BaseNode {
    public DeterministicLlmNode(ChatModel deterministicModel) {
        super(callLlm(deterministicModel));
    }

    private static Function<Dict, Dict> callLlm(ChatModel deterministicModel) {
        return input -> {
            ChatResponse call = deterministicModel.call(input.getPrompt());
            return Dict.sin(call);
        };
    }

}
