package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.llm.LlmNode;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class DeterministicLlmNode extends LlmNode {
    public DeterministicLlmNode(ChatModel deterministicModel) {
        super(deterministicModel);
    }
}
