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
    private Long chatId;
    private Long groupId;
    private String senderUsername;
    private String senderAvatarUrl;
    private String senderAvatarColor;
    private String senderAvatarInitials;

    public static MessageDto fromEntity(Message message) {
        Long chatId = message.getChat() != null ? message.getChat().getId() : null;
        Long groupId = message.getGroup() != null ? message.getGroup().getId() : null;
        Long receiverId = message.getReceiver() != null ? message.getReceiver().getId() : null;

        MessageDto dto = new MessageDto(
                message.getSender().getId(),
                receiverId,
                message.getContent(),
                message.getTimestamp(),
                chatId,
                groupId
        );
        dto.setSenderUsername(message.getSender().getUsernameForController());
        dto.setSenderUsername(message.getSender().getUsernameForController());
        dto.setSenderAvatarUrl(message.getSender().getAvatarUrl());
        dto.setSenderAvatarColor(message.getSender().getAvatarColor());
        dto.setSenderAvatarInitials(message.getSender().getAvatarInitials());

        return dto;
    }

    public static List<MessageDto> fromEntities(List<Message> messages) {
        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    public MessageDto() {}

    public MessageDto(Long senderId, Long receiverId, String content, LocalDateTime timestamp, Long chatId, Long groupId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.chatId = chatId;
        this.groupId = groupId;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }

    public String getSenderAvatarColor() {
        return senderAvatarColor;
    }

    public void setSenderAvatarColor(String senderAvatarColor) {
        this.senderAvatarColor = senderAvatarColor;
    }

    public String getSenderAvatarInitials() {
        return senderAvatarInitials;
    }

    public void setSenderAvatarInitials(String senderAvatarInitials) {
        this.senderAvatarInitials = senderAvatarInitials;
    }
    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
