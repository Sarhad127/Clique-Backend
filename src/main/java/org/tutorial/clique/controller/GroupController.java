package org.tutorial.clique.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.clique.dto.GroupDto;
import org.tutorial.clique.model.Group;
import org.tutorial.clique.model.User;
import org.tutorial.clique.repository.GroupRepository;
import org.tutorial.clique.repository.UserRepository;
import org.tutorial.clique.service.JwtService;

import java.util.HashSet;
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

    private GroupDto toDto(Group group) {
        Set<Long> userIds = group.getUsers() != null
                ? group.getUsers().stream().map(User::getId).collect(Collectors.toSet())
                : new HashSet<>();
        return new GroupDto(
                group.getId(),
                group.getTitle(),
                userIds
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
        if (groupDto.getUserIds() != null && !groupDto.getUserIds().isEmpty()) {
            users = groupDto.getUserIds().stream()
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
}