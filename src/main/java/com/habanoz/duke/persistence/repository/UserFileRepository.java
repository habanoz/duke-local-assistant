package com.habanoz.duke.persistence.repository;

import com.habanoz.duke.persistence.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserFileRepository extends JpaRepository<UserFile, UUID> {
    List<UserFile> findByUserIdAndChatId(UUID userId, UUID chatId);
    List<UserFile> findByUserId(UUID userId);

    void deleteByChatId(UUID chatId);
}
