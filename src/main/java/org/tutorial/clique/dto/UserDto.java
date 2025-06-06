package org.tutorial.clique.dto;

import java.util.Set;

public class UserDto {

    private Long id;
    private String email;
    private String username;
    private String avatarUrl;
    private String avatarColor;
    private String avatarInitials;
    private String description;
    private Set<FriendDto> friends;

    public UserDto() {}

    public UserDto(Long id, String email, String username,
                   String avatarUrl, String avatarColor, String avatarInitials,
                   Set<FriendDto> friends, String description) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.avatarColor = avatarColor;
        this.avatarInitials = avatarInitials;
        this.friends = friends;
        this.description = description;
    }

    public Set<FriendDto> getFriends() {
        return friends;
    }

    public void setFriends(Set<FriendDto> friends) {
        this.friends = friends;
    }

    public String getAvatarInitials() {
        return avatarInitials;
    }

    public void setAvatarInitials(String avatarInitials) {
        this.avatarInitials = avatarInitials;
    }

    public String getAvatarColor() {
        return avatarColor;
    }

    public void setAvatarColor(String avatarColor) {
        this.avatarColor = avatarColor;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
