package com.habanoz.duke.service;

import com.habanoz.duke.persistence.entity.UserFile;
import com.habanoz.duke.persistence.entity.UserFileChunk;
import com.habanoz.duke.persistence.repository.UserFileChunkRepository;
import com.habanoz.duke.persistence.repository.UserFileRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class FilesService {
    public static final UUID EMPTY_CHAT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private UserFileChunkRepository userFileChunkRepository;


    @Transactional
    public void fileUpload(UUID userId, UUID chatId, Resource resource) throws IOException {
        TextSplitter textSplitter = new TokenTextSplitter(300, 200, 5, 1000, true);

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
        List<Document> docs = tikaDocumentReader.read();
        List<Document> splittedDocs = textSplitter.apply(docs);

        vectorStore.add(splittedDocs);

        UUID fileId = UUID.randomUUID();
        userFileRepository.save(new UserFile(fileId, chatId, userId, resource.getFilename(), null, resource.contentLength()));
        userFileChunkRepository.saveAll(splittedDocs.stream().map(s -> new UserFileChunk(UUID.randomUUID(), fileId, chatId)).toList());
    }

    public List<UserFile> getFiles(UUID userId, UUID chatId) {
        List<UserFile> userFiles = userFileRepository.findByUserIdAndChatId(userId, EMPTY_CHAT_ID);
        List<UserFile> chatFiles = userFileRepository.findByUserIdAndChatId(userId, chatId);

        return Stream.concat(userFiles.stream(), chatFiles.stream()).toList();
    }

    @Transactional
    public void deleteChat(UUID chatId){
        userFileRepository.deleteByChatId(chatId);
        userFileChunkRepository.deleteByChatId(chatId);
    }

}
