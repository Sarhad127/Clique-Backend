package org.tutorial.clique.dto;

import java.util.Set;

public class UserDto {

    private Long id;
    private String email;
    private String username;
    private String avatarUrl;
    private String avatarColor;
    private String avatarInitials;
    private Set<Long> friendIds;
    private Set<Long> serverIds;

    public UserDto() {}

    public UserDto(Long id, String email, String username,
                   String avatarUrl, String avatarColor, String avatarInitials,
                   Set<Long> friendIds, Set<Long> serverIds) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.avatarColor = avatarColor;
        this.avatarInitials = avatarInitials;
        this.friendIds = friendIds;
        this.serverIds = serverIds;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getAvatarUrl() { return avatarUrl; }

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getAvatarColor() { return avatarColor; }

    public void setAvatarColor(String avatarColor) { this.avatarColor = avatarColor; }

    public String getAvatarInitials() { return avatarInitials; }

    public void setAvatarInitials(String avatarInitials) { this.avatarInitials = avatarInitials; }

    public Set<Long> getFriendIds() { return friendIds; }

    public void setFriendIds(Set<Long> friendIds) { this.friendIds = friendIds; }

    public Set<Long> getServerIds() { return serverIds; }

    public void setServerIds(Set<Long> serverIds) { this.serverIds = serverIds; }
}
