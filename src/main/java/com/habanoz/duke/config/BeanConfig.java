package com.habanoz.duke.config;

import com.habanoz.duke.graph.WebSearchNode;
import com.habanoz.duke.tool.WebSearchRetriever;
import org.springframework.ai.autoconfigure.ollama.OllamaChatProperties;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
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
    public WebSearchRetriever webSearch() {
        return new WebSearchRetriever();
    }
}
