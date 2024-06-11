package com.habanoz.duke.persistence.entity;

import com.habanoz.duke.controller.model.ChatRole;
import com.habanoz.duke.controller.model.IChatMessage;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "utterance_history",
        indexes = {@Index(name = "utterance_history_chat_id", columnList = "chatId")}
)
public class Utterance {
    @Id
    private UUID id;
    private UUID chatId;
    private UUID userId;

    @Column(length = 1024)
    private String content;
    private ChatRole role;

    @JdbcTypeCode(SqlTypes.JSON)
    private IChatMessage chatMessage;

    @CreatedDate
    private LocalDateTime cdate;

    public Utterance() {
    }

    public Utterance(UUID id, UUID chatId, UUID userId, String content, ChatRole role, IChatMessage chatMessage) {
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.content = content;
        this.role = role;
        this.chatMessage = chatMessage;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatRole getRole() {
        return role;
    }

    public void setRole(ChatRole role) {
        this.role = role;
    }

    public LocalDateTime getCdate() {
        return cdate;
    }

    public void setCdate(LocalDateTime cdate) {
        this.cdate = cdate;
    }

    public IChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(IChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utterance utterance = (Utterance) o;
        return Objects.equals(id, utterance.id) && Objects.equals(chatId, utterance.chatId) && Objects.equals(userId, utterance.userId) && Objects.equals(content, utterance.content) && role == utterance.role && Objects.equals(cdate, utterance.cdate) && Objects.equals(chatMessage, utterance.chatMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, userId, content, role, cdate, chatMessage);
    }
}
