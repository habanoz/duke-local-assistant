package com.habanoz.duke.graph.utils;

import com.habanoz.duke.core.model.Dict;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.List;

public class FormatUtils {
    public static Dict formatDocs(Dict input) {
        List<Document> docs = input.getVal("docs");
        List<String> str_docs = new ArrayList<>();

        String formattedDocs = formatDocuments(docs, str_docs);

        return input.extend("context", formattedDocs);
    }

    public static String formatDocuments(List<Document> docs, List<String> str_docs) {
        for (int i = 0; i < docs.size(); i++) {
            Document doc = docs.get(i);
            str_docs.add("<doc id='%s'>%s</doc>".formatted(i + 1, doc.getContent()));
        }
        return String.join("\n", str_docs);
    }

    public static Dict formatChatHistory(Dict input) {
        List<Message> chatHistory = input.getVal("chatHistory");
        String formattedDocs = formatChatHistory(chatHistory);

        return input.extend("chatHistoryStr", formattedDocs);
    }

    public static String formatChatHistory(List<Message> chatHistory) {
        List<String> str_docs = new ArrayList<>();

        for (Message doc : chatHistory) {
            str_docs.add("- %s: %s".formatted(doc.getMessageType().name(), doc.getContent()));
        }
        return String.join("\n", str_docs);
    }
}
