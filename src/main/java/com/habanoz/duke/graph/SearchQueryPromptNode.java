package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.prompt.PromptTemplateNode;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchQueryPromptNode extends PromptTemplateNode {
    public SearchQueryPromptNode(List<PromptTemplate> searchQueryTemplates) {
        super(searchQueryTemplates);
    }
}
