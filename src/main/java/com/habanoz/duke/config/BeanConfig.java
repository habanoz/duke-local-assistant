package com.habanoz.duke.config;

import org.springframework.ai.autoconfigure.ollama.OllamaChatProperties;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Value("${spring.ai.ollama.base-url}")
    private String baseUrl;

    @Bean
    @ConfigurationProperties("spring.ai.ollama.chat.answer")
    public OllamaChatProperties answerModelChatProperties() {
        return new OllamaChatProperties();
    }

    @Bean
    @ConfigurationProperties("spring.ai.ollama.chat.deterministic")
    public OllamaChatProperties deterministicModelChatProperties() {
        return new OllamaChatProperties();
    }

    @Bean
    public OllamaChatModel answerModel() {
        OllamaApi ollamaApi = new OllamaApi(baseUrl);
        return new OllamaChatModel(ollamaApi, answerModelChatProperties().getOptions());
    }

    @Bean
    public OllamaChatModel deterministicModel() {
        OllamaApi ollamaApi = new OllamaApi(baseUrl);
        return new OllamaChatModel(ollamaApi, deterministicModelChatProperties().getOptions());
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }
}
