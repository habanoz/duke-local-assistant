package com.habanoz.duke.controller.model;

public record ICreateChatSessionResponse(IChatSession chatSession, IChatMessage initialBotMessage) {
}
