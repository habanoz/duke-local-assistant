package com.habanoz.duke.controller.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record IChatMessage(UUID id, UUID chatId, UUID userId, String userName, String content, String prompt, List<Citation> citations,
                           ChatRole role, LocalDateTime timestamp) {
}
