package com.habanoz.duke.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_file_chunks",
        indexes = {@Index(name = "user_file_chunks_file_id_idx", columnList = "fileId"),
                @Index(name = "user_file_chunks_chat_id_idx", columnList = "chatId")}
)
public class UserFileChunk {
    @Id
    private UUID id;
    private UUID fileId;
    private UUID chatId;

    @CreatedDate
    private LocalDateTime createdOn;

    public UserFileChunk() {
    }

    public UserFileChunk(UUID id, UUID fileId, UUID chatId) {
        this.id = id;
        this.fileId = fileId;
        this.chatId = chatId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFileChunk that = (UserFileChunk) o;
        return Objects.equals(id, that.id) && Objects.equals(fileId, that.fileId) && Objects.equals(chatId, that.chatId) && Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileId, chatId, createdOn);
    }

    @Override
    public String toString() {
        return "UserFileChunk{" +
                "id=" + id +
                ", fileId=" + fileId +
                ", chatId=" + chatId +
                ", createdOn=" + createdOn +
                '}';
    }
}