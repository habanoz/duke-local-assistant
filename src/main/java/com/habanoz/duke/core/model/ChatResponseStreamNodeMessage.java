package com.habanoz.duke.core.model;

import org.springframework.ai.chat.model.ChatResponse;

public record ChatResponseStreamNodeMessage(ChatResponse chatResponse) implements StreamNodeMessage {
}
