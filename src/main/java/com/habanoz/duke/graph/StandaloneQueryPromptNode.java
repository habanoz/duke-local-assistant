package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.prompt.PromptTemplateNode;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class StandaloneQueryPromptNode extends PromptTemplateNode {
    public StandaloneQueryPromptNode(@Value("classpath:/prompts/standalone.st") Resource standaloneQueryTemplate) {
        super(new PromptTemplate(standaloneQueryTemplate));
    }
}
