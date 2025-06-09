package org.tutorial.clique.dto;

import org.tutorial.clique.model.Chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatDto {

    private Long id;
    private Set<FriendDto> participants;
    private List<MessageDto> messages;
    private LocalDateTime createdAt;

    public ChatDto() {}

    public ChatDto(Long id, Set<FriendDto> participants, List<MessageDto> messages, LocalDateTime createdAt) {
        this.id = id;
        this.participants = participants;
        this.messages = messages;
        this.createdAt = createdAt;
    }

    public static ChatDto fromEntity(Chat chat) {
        return new ChatDto(
                chat.getId(),
                chat.getParticipants().stream()
                        .map(user -> new FriendDto(
                                user.getId(),
                                user.getEmail(),
                                user.getAvatarInitials(),
                                user.getAvatarColor(),
                                user.getAvatarUrl(),
                                user.getUsernameForController(),
                                user.getDescription()))
                        .collect(Collectors.toSet()),
                chat.getMessages().stream()
                        .map(MessageDto::fromEntity)
                        .collect(Collectors.toList()),
                chat.getCreatedAt()
        );
    }

    public static List<ChatDto> fromEntities(List<Chat> chats) {
        return chats.stream()
                .map(ChatDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<FriendDto> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<FriendDto> participants) {
        this.participants = participants;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
