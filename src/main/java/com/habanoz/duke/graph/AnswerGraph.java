package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.aux.ActionNone;
import com.habanoz.duke.core.graph.aux.AssignNode;
import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.graph.parser.StringOutParser;
import com.habanoz.duke.core.graph.prompt.PromptTemplateNode;
import com.habanoz.duke.core.model.Dict;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.AssistantPromptTemplate;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnswerGraph {
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

    @Autowired
    private WebSearchNode webSearch;

    private Dict formatDocs(Dict input) {
        List<Document> docs = input.getVal("docs");
        List<String> str_docs = new ArrayList<>();

        for (int i = 0; i < docs.size(); i++) {
            Document doc = docs.get(i);
            str_docs.add("<doc id='%s'>%s</doc>".formatted(i + 1, doc.getContent()));
        }
        String formattedDocs = String.join("\n", str_docs);

        return input.extend("context", formattedDocs);
    }

    private Dict formatChatHistory(Dict input) {
        List<Message> chatHistory = input.getVal("chatHistory");
        List<String> str_docs = new ArrayList<>();

        for (Message doc : chatHistory) {
            str_docs.add("- %s: %s".formatted(doc.getMessageType().name(), doc.getContent()));
        }
        String formattedDocs = String.join("\n", str_docs);

        return input.extend("chatHistoryStr", formattedDocs);
    }

    public String call(List<Message> messages) {
        StringOutParser strParser = new StringOutParser();

        BaseNode standaloneQueryGraph = new ActionNone(this::formatChatHistory).chain(standaloneQueryPrompt).chain(deterministicLlm).chain(strParser);
        BaseNode searchGraph = searchQueryPrompt.chain(deterministicLlm).chain(strParser).chain(webSearch);

        BaseNode graph = new AssignNode("standaloneQuery", standaloneQueryGraph).chain(new AssignNode("docs", searchGraph)).chain(new ActionNone(this::formatDocs));

        String question = messages.getLast().getContent();
        List<Message> chatHistory = messages.subList(0, messages.size() - 1);

        List<PromptTemplate> promptTemplates = getAnswerPromptTemplates(chatHistory);
        PromptTemplateNode answerPrompt = new PromptTemplateNode(promptTemplates);

        Dict input = Dict.map("question", question, "chatHistory", chatHistory);

        return graph.chain(answerPrompt).chain(answerLlm).chain(strParser).call(input).str();
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

    public BaseNode searchQuery() {
        return null;
    }

    public BaseNode webSearch() {
        return null;
    }

    public BaseNode standaloneQuery() {
        return null;
    }
}
