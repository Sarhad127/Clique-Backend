package org.tutorial.clique.dto;

public class GroupMemberDto {
    private Long id;
    private String username;
    private String avatarUrl;
    private String avatarColor;
    private String avatarInitials;

    public GroupMemberDto(Long id, String username, String avatarUrl, String avatarColor, String avatarInitials) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.avatarColor = avatarColor;
        this.avatarInitials = avatarInitials;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}