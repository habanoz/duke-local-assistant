package com.habanoz.duke.controller.rest;

import com.habanoz.duke.persistence.entity.UserFile;
import com.habanoz.duke.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})
@RestController
public class DocumentsController {

    @Autowired
    private FilesService filesService;

    @PostMapping(value = "{userId}/chats/{chatId}/documents", consumes = {"multipart/form-data"})
    public Map<String, String> postChatDocuments(
            @PathVariable("userId") UUID userId,
            @PathVariable("chatId") UUID chatId,
            @RequestPart("useContentSafety") String useContentSafety,
            @RequestPart("formFiles") MultipartFile formFiles) throws IOException {
        filesService.fileUpload(userId, chatId, formFiles.getResource());

        return Map.of("result", "ok");
    }

    @GetMapping(value = "{userId}/chats/{chatId}/documents")
    public List<UserFile> getChatDocuments(@PathVariable("userId") UUID userId, @PathVariable("chatId") UUID chatId) {
        return filesService.getFiles(userId, chatId);
    }
}
