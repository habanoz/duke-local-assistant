package com.habanoz.duke.core.graph.prompt;

import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.Dict;
import com.habanoz.duke.core.model.NodeFunction;
import com.habanoz.duke.core.model.PromptMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Collections;
import java.util.List;

public class PromptTemplateNode extends SimpleNode {
    public PromptTemplateNode(PromptTemplate template) {
        super(getFunction(Collections.singletonList(template)));
    }

    public PromptTemplateNode(List<PromptTemplate> templates) {
        super(getFunction(templates));
    }

    private static NodeFunction getFunction(List<PromptTemplate> templates) {
        return (dict, eventSink) -> dict.map(d -> {
            if (d instanceof Dict dm) {
                return new PromptMessage((new Prompt(templates.stream().map(template -> template.createMessage(dm.map())).toList())));
            }

            throw new IllegalArgumentException("Only Dict messages are expected");
        });
    }
}
