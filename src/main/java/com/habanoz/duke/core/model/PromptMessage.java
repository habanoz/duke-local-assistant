package com.habanoz.duke.core.model;

import org.springframework.ai.chat.prompt.Prompt;

public record PromptMessage(Prompt prompt) implements NodeMessage{

}
