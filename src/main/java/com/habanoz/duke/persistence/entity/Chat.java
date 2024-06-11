package com.habanoz.duke.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "chat_history", indexes = @Index(name = "chat_history_user_id_idx", columnList = "userId"))
public class Chat {
    @Id
    private UUID id;
    private UUID userId;
    private String title;
    @CreatedDate
    private LocalDateTime cdate;


    public Chat(UUID id, UUID userId, String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
    }

    public Chat() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCdate() {
        return cdate;
    }

    public void setCdate(LocalDateTime cdate) {
        this.cdate = cdate;
    }
}
