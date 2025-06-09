package org.tutorial.clique.dto;

import java.util.Set;

public class GroupDto {

    private Long id;
    private String title;
    private Set<GroupMemberDto> members;
    private String backgroundImageUrl;

    public GroupDto() {}

    public GroupDto(Long id, String title, Set<GroupMemberDto> members) {
        this.id = id;
        this.title = title;
        this.members = members;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Set<GroupMemberDto> getMembers() { return members; }
    public void setMembers(Set<GroupMemberDto> members) { this.members = members; }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }
    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }
}