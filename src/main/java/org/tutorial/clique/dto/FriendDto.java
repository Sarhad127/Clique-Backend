package org.tutorial.clique.dto;

public class FriendDto {
    private Long id;
    private String username;
    private String avatarUrl;
    private String avatarColor;
    private String avatarInitials;
    private String email;
    private String description;

    public FriendDto() {}

    public FriendDto(Long id, String email, String avatarInitials, String avatarColor, String avatarUrl, String username, String description) {
        this.id = id;
        this.email = email;
        this.avatarInitials = avatarInitials;
        this.avatarColor = avatarColor;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
