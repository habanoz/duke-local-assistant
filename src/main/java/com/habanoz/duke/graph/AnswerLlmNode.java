package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AnswerLlmNode extends BaseNode {
    public AnswerLlmNode(ChatModel answerModel) {
        super(callLlm(answerModel));
    }

    private static Function<Dict, Dict> callLlm(ChatModel chatModel) {
        return input -> {
            ChatResponse call = chatModel.call(input.getPrompt());
            return Dict.sin(call);
        };
    }

}
