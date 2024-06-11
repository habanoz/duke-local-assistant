package com.habanoz.duke.controller.rest;

import com.habanoz.duke.controller.model.CreateChatBody;
import com.habanoz.duke.controller.model.IChatMessage;
import com.habanoz.duke.controller.model.IChatSession;
import com.habanoz.duke.controller.model.ICreateChatSessionResponse;
import com.habanoz.duke.service.ChatService;
import com.habanoz.duke.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})
@RestController
public class ChatHistoryController {

    @Autowired
    private ChatService chatService;


    @Autowired
    private FilesService filesService;

    @PostMapping(value = "{userId}/chats")
    public ICreateChatSessionResponse createChat(@PathVariable("userId") UUID userId, @RequestBody CreateChatBody body) {
        return chatService.addChat(userId, body.title());
    }

    @GetMapping(value = "/chats/{chatId}")
    public IChatSession getChat(@PathVariable("chatId") UUID chatId) {
        return chatService.getChat(chatId).orElse(null);
    }

    @DeleteMapping(value = "/chats/{chatId}")
    public Map<String, String> deleteChat(@PathVariable UUID chatId) {
        chatService.deleteChat(chatId);
        filesService.deleteChat(chatId);
        return Map.of("result", "ok");
    }

    @GetMapping(value = "{userId}/chats")
    public List<IChatSession> getChats(@PathVariable("userId") UUID userId) {
        return chatService.getChats(userId);
    }

    @GetMapping(value = "chats/{chatId}/messages")
    public List<IChatMessage> getMessages(@PathVariable("chatId") UUID chatId,
                                          @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                          @RequestParam(value = "size", defaultValue = "100", required = false) Integer size) {
        return chatService.getChatMessages(chatId, page, size);
    }

}
