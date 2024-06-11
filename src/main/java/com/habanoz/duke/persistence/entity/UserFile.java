package com.habanoz.duke.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_files", indexes = {@Index(name = "user_files_chat_id_idx", columnList = "chatId"),
        @Index(name = "user_files_user_id_chat_id_idx", columnList = "userId,chatId")})
public class UserFile {
    @Id
    private UUID id;

    @Column(nullable = true)
    private UUID chatId;

    private UUID userId;
    private String name;
    private String hyperlink;

    @CreatedDate
    private LocalDateTime createdOn;
    private long size;

    public UserFile() {
    }

    public UserFile(UUID id, UUID chatId, UUID userId, String name, String hyperlink, long size) {
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.name = name;
        this.hyperlink = hyperlink;
        this.size = size;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String name() {
        return name;
    }

    public String hyperlink() {
        return hyperlink;
    }


    public LocalDateTime createdOn() {
        return createdOn;
    }

    public long size() {
        return size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserFile) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.chatId, that.chatId) &&
                Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.hyperlink, that.hyperlink) &&
                this.createdOn == that.createdOn &&
                this.size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, userId, name, hyperlink, createdOn, size);
    }

    @Override
    public String toString() {
        return "ChatMemorySource[" +
                "id=" + id + ", " +
                "chatId=" + chatId + ", " +
                "userId=" + userId + ", " +
                "name=" + name + ", " +
                "hyperlink=" + hyperlink + ", " +
                "createdOn=" + createdOn + ", " +
                "size=" + size + ']';
    }


}