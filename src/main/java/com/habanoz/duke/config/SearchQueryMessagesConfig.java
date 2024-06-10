package com.habanoz.duke.config;

import org.springframework.ai.chat.prompt.AssistantPromptTemplate;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:prompts/search-query.yml", factory = YamlPropertySourceFactory.class)
public class SearchQueryMessagesConfig {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Bean
    public ArrayList<PromptTemplate> searchQueryTemplates() {
        ArrayList<PromptTemplate> chatMessages = new ArrayList<>(messages.size());
        for (Message message : messages) {
            if ("system".equalsIgnoreCase(message.getRole())) {
                chatMessages.add(new SystemPromptTemplate(message.getContent()));
            } else if ("user".equalsIgnoreCase(message.getRole())) {
                chatMessages.add(new PromptTemplate(message.getContent()));
            } else if ("assistant".equalsIgnoreCase(message.getRole())) {
                chatMessages.add(new AssistantPromptTemplate(message.getContent()));
            } else {
                throw new IllegalArgumentException("Unsupported role:" + message.getRole());
            }
        }
        return chatMessages;
    }

    public static class Message {
        private String role;
        private String content;

        // Getters and setters for type and content
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
