package com.habanoz.duke.persistence.repository;

import com.habanoz.duke.persistence.entity.Utterance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtteranceRepository extends JpaRepository<Utterance, UUID> {
    Slice<Utterance> findByChatId(UUID chatId, Pageable pageable);

    void deleteByChatId(UUID chatId);
}
