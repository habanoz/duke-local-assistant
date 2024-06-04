package com.habanoz.duke.core.graph.prompt;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PromptTemplateNode extends BaseNode {
    public PromptTemplateNode(PromptTemplate template) {
        super(getFunction(Collections.singletonList(template)));
    }

    public PromptTemplateNode(List<PromptTemplate> templates) {
        super(getFunction(templates));
    }

    private static Function<Dict, Dict> getFunction(List<PromptTemplate> templates) {
        return (dict) -> Dict.sin(new Prompt(templates.stream().map(template -> template.createMessage(dict.map())).toList()));
    }
}
