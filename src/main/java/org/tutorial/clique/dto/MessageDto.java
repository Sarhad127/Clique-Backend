package org.tutorial.clique.dto;

import org.tutorial.clique.model.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MessageDto {

    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;

    public MessageDto() {}

    public MessageDto(Long senderId, Long receiverId, String content, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static MessageDto fromEntity(Message message) {
        return new MessageDto(
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getContent(),
                message.getTimestamp()
        );
    }

    public static List<MessageDto> fromEntities(List<Message> messages) {
        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
