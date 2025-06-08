package org.tutorial.clique.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.clique.dto.GroupDto;
import org.tutorial.clique.dto.GroupMemberDto;
import org.tutorial.clique.dto.InviteRequest;
import org.tutorial.clique.model.Group;
import org.tutorial.clique.model.User;
import org.tutorial.clique.repository.GroupRepository;
import org.tutorial.clique.repository.UserRepository;
import org.tutorial.clique.service.JwtService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public GroupController(GroupRepository groupRepository, UserRepository userRepository, JwtService jwtService) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    private GroupMemberDto toGroupMemberDto(User user) {
        return new GroupMemberDto(
                user.getId(),
                user.getUsernameForController(),
                user.getAvatarUrl(),
                user.getAvatarColor(),
                user.getAvatarInitials()
        );
    }

    private GroupDto toDto(Group group) {
        Set<GroupMemberDto> members = group.getUsers() != null
                ? group.getUsers().stream()
                .map(this::toGroupMemberDto)
                .collect(Collectors.toSet())
                : new HashSet<>();

        return new GroupDto(
                group.getId(),
                group.getTitle(),
                members
        );
    }

    @GetMapping
    public ResponseEntity<?> getUserGroups(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractUsername(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOpt.get();

        Set<GroupDto> userGroups = user.getGroups()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(userGroups);
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupDto groupDto, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractUsername(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> creatorOpt = userRepository.findByEmail(email);
        if (creatorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User creator = creatorOpt.get();

        Set<User> users = new HashSet<>();
        if (groupDto.getMembers() != null && !groupDto.getMembers().isEmpty()) {
            users = groupDto.getMembers().stream()
                    .map(GroupMemberDto::getId)
                    .map(userRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
        }

        users.add(creator);

        Group group = new Group();
        group.setTitle(groupDto.getTitle());
        group.setUsers(users);

        groupRepository.save(group);

        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(group));
    }

    @PostMapping("/{groupId}/invite")
    public ResponseEntity<?> inviteUserToGroup(
            @PathVariable Long groupId,
            @RequestBody InviteRequest inviteRequest,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractUsername(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> inviterOpt = userRepository.findByEmail(email);
        if (inviterOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User inviter = inviterOpt.get();
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
        Group group = groupOpt.get();
        if (!group.getUsers().contains(inviter)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not a member of this group");
        }
        Optional<User> userToInviteOpt = userRepository.findByEmail(inviteRequest.getUserIdentifier());
        if (userToInviteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User to invite not found");
        }
        User userToInvite = userToInviteOpt.get();
        if (group.getUsers().contains(userToInvite)) {
            return ResponseEntity.badRequest().body("User is already a member of the group");
        }
        group.getUsers().add(userToInvite);
        groupRepository.save(group);
        return ResponseEntity.ok(Map.of("message", "User invited successfully"));
    }
}