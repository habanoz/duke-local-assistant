package com.habanoz.duke.controller.model;

import java.util.List;

public record ChatResponseRequest(List<SimpleChatMessage> messages, String userName) {
}
