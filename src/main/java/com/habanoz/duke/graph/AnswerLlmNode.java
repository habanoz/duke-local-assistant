package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.llm.LlmNode;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class AnswerLlmNode extends LlmNode {
    public AnswerLlmNode(ChatModel answerModel) {
        super(answerModel);
    }
}
