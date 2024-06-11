package com.habanoz.duke.service;

import com.habanoz.duke.controller.model.ChatRole;
import com.habanoz.duke.controller.model.IChatMessage;
import com.habanoz.duke.controller.model.IChatSession;
import com.habanoz.duke.controller.model.ICreateChatSessionResponse;
import com.habanoz.duke.persistence.entity.Chat;
import com.habanoz.duke.persistence.entity.Utterance;
import com.habanoz.duke.persistence.repository.ChatRepository;
import com.habanoz.duke.persistence.repository.UtteranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ChatService {
    public static final UUID BOT_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final String BOT_USERNAME = "bot";

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UtteranceRepository utteranceRepository;

    @Transactional
    public ICreateChatSessionResponse addChat(UUID userId, String title) {
        Chat chat = new Chat(UUID.randomUUID(), userId, title);
        chatRepository.save(chat);

        UUID messageId = UUID.randomUUID();
        IChatMessage initialBotMessage = new IChatMessage(messageId, chat.getId(), BOT_USER_ID, BOT_USERNAME, "Hello, how can i help you?", null, null, ChatRole.ASSISTANT, LocalDateTime.now());
        utteranceRepository.save(new Utterance(initialBotMessage.id(), chat.getId(), userId, initialBotMessage.content(), initialBotMessage.role(), initialBotMessage));

        IChatSession chatSession = new IChatSession(chat.getId(), chat.getTitle());
        return new ICreateChatSessionResponse(chatSession, initialBotMessage);
    }

    @Transactional
    public void addUtterances(UUID userId, String userName, UUID chatId, UUID botMessageId, String question, String botAnswer) {
        UUID messageId = UUID.randomUUID();
        addUserUtterance(messageId, chatId, userId, userName, question);
        addAssistantUtterance(botMessageId, chatId, botAnswer);
    }

    @Transactional
    public void addUserUtterance(UUID messageId, UUID chatId, UUID userId, String userName, String content) {
        IChatMessage userMessage = new IChatMessage(messageId, chatId, userId, userName, content, null, null, ChatRole.USER, LocalDateTime.now());
        String clippedContent = content.substring(0, Math.min(content.length(), 1024));

        utteranceRepository.save(new Utterance(messageId, chatId, userId, clippedContent, ChatRole.USER, userMessage));
    }

    @Transactional
    public void addAssistantUtterance(UUID messageId, UUID chatId, String content) {
        IChatMessage botMessage = new IChatMessage(messageId, chatId, BOT_USER_ID, BOT_USERNAME, content, null, null, ChatRole.ASSISTANT, LocalDateTime.now());
        String clippedContent = content.substring(0, Math.min(content.length(), 1024));

        utteranceRepository.save(new Utterance(messageId, chatId, BOT_USER_ID, clippedContent, ChatRole.ASSISTANT, botMessage));
    }

    @Transactional
    public void deleteChat(UUID chatId) {
        chatRepository.deleteById(chatId);
        utteranceRepository.deleteByChatId(chatId);
    }

    public Optional<IChatSession> getChat(UUID chatId) {
        Optional<Chat> oChat = chatRepository.findById(chatId);
        if (oChat.isEmpty()) return Optional.empty();

        Chat chat = oChat.get();

        return Optional.of(new IChatSession(chatId, chat.getTitle()));
    }

    public List<IChatSession> getChats(UUID userId) {
        List<Chat> chats = chatRepository.findByUserId(userId);
        return chats.stream().map(s -> new IChatSession(s.getId(), s.getTitle())).toList();
    }

    public List<IChatMessage> getChatMessages(UUID chatId, Integer page, Integer size) {
        return utteranceRepository.findByChatId(chatId, PageRequest.of(page, size).withSort(Sort.by("cdate").descending()))
                .stream().map(Utterance::getChatMessage).collect(Collectors.toList());
    }

}
