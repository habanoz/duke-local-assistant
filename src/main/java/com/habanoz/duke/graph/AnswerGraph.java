package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.graph.help.ActionNode;
import com.habanoz.duke.core.graph.help.AssignNode;
import com.habanoz.duke.core.graph.parser.StringOutParser;
import com.habanoz.duke.core.graph.prompt.PromptTemplateNode;
import com.habanoz.duke.core.model.*;
import com.habanoz.duke.graph.utils.FormatUtils;
import com.habanoz.duke.tool.WebSearchRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.AssistantPromptTemplate;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnswerGraph {
    private static final Logger log = LoggerFactory.getLogger(AnswerGraph.class);

    @Value("classpath:/prompts/system-message.st")
    private Resource answerSystemResource;

    @Autowired
    private AnswerLlmNode answerLlm;

    @Autowired
    private DeterministicLlmNode deterministicLlm;

    @Autowired
    private SearchQueryPromptNode searchQueryPrompt;

    @Autowired
    private StandaloneQueryPromptNode standaloneQueryPrompt;


    public String call(List<Message> messages) {
        String question = messages.getLast().getContent();
        List<Message> chatHistory = messages.subList(0, messages.size() - 1);
        Dict input = Dict.map("question", question, "chatHistory", chatHistory);

        EventPublisher eventPublisher = new LoggingEventPublisher();
        return getGraph(messages, eventPublisher).stream(Flux.just(input), eventPublisher).map(d -> (ANodeMessage) d).blockFirst().str();
    }

    public Flux<NodeMessage> stream(List<Message> messages, EventPublisher eventPublisher) {
        String question = messages.getLast().getContent();
        List<Message> chatHistory = messages.subList(0, messages.size() - 1);
        Dict input = Dict.map("question", question, "chatHistory", chatHistory);

        return getGraph(messages, eventPublisher).stream(Flux.just(input), eventPublisher);
    }

    private BaseNode getGraph(List<Message> messages, EventPublisher eventPublisher) {
        StringOutParser strParser = new StringOutParser();

        BaseNode standaloneQueryGraph = new ActionNode(FormatUtils::formatChatHistory).chain(standaloneQueryPrompt).chain(deterministicLlm).chain(strParser);
        BaseNode searchGraph = searchQueryPrompt.chain(deterministicLlm).chain(strParser).chain(new WebSearchNode(new WebSearchRetriever(eventPublisher)));

        BaseNode graph = new AssignNode("standaloneQuery", standaloneQueryGraph).chain(new AssignNode("docs", searchGraph)).chain(new ActionNode(FormatUtils::formatDocs));

        PromptTemplateNode answerPrompt = getAnswerPrompt(messages);

        return graph.chain(answerPrompt).chain(answerLlm).chain(strParser);
    }

    private PromptTemplateNode getAnswerPrompt(List<Message> messages) {
        List<Message> chatHistory = messages.subList(0, messages.size() - 1);
        List<PromptTemplate> promptTemplates = getAnswerPromptTemplates(chatHistory);

        return new PromptTemplateNode(promptTemplates);
    }

    private List<PromptTemplate> getAnswerPromptTemplates(List<Message> chatHistory) {
        List<PromptTemplate> promptTemplates = new ArrayList<>();
        promptTemplates.add(new SystemPromptTemplate(answerSystemResource));
        for (Message message : chatHistory) {
            if (message.getMessageType() == MessageType.ASSISTANT)
                promptTemplates.add(new AssistantPromptTemplate(message.getContent()));
            else if (message.getMessageType() == MessageType.USER)
                promptTemplates.add(new PromptTemplate(message.getContent()));
            else throw new IllegalArgumentException("Unknown message type:" + message.getMessageType());
        }
        promptTemplates.add(new PromptTemplate("{question}"));

        return promptTemplates;
    }
}
