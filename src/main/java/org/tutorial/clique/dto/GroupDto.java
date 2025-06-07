package org.tutorial.clique.dto;

import java.util.Set;

public class GroupDto {

    private Long id;
    private String title;
    private Set<Long> userIds;

    public GroupDto() {}

    public GroupDto(Long id, String title, Set<Long> userIds) {
        this.id = id;
        this.title = title;
        this.userIds = userIds;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Set<Long> getUserIds() { return userIds; }
    public void setUserIds(Set<Long> userIds) { this.userIds = userIds; }
}