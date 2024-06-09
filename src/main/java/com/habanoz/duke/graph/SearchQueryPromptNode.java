package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.prompt.PromptTemplateNode;
import com.habanoz.duke.core.model.Event;
import com.habanoz.duke.core.model.NodeMessage;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchQueryPromptNode extends PromptTemplateNode {
    public SearchQueryPromptNode(List<PromptTemplate> searchQueryTemplates) {
        super(searchQueryTemplates);
    }

    @Override
    public Event getEvent(NodeMessage e) {
        return new Event("StatusUpdate", "Extracting Search Query");
    }
}
