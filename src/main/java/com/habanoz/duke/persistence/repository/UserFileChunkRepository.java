package com.habanoz.duke.persistence.repository;

import com.habanoz.duke.persistence.entity.UserFileChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserFileChunkRepository extends JpaRepository<UserFileChunk, UUID> {
    void deleteByChatId(UUID chatId);

    void deleteByFileId(UUID fileId);
}
