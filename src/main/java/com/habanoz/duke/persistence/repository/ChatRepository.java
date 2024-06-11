package com.habanoz.duke.persistence.repository;

import com.habanoz.duke.persistence.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findByUserId(UUID userId);
}
