package org.tutorial.clique.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.tutorial.clique.configuration.MyUserDetailService;
import org.tutorial.clique.dto.FriendDto;
import org.tutorial.clique.dto.UserDto;
import org.tutorial.clique.model.User;
import org.tutorial.clique.repository.UserRepository;
import org.tutorial.clique.service.JwtService;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MyUserDetailService userDetailsService;

    public UserController(UserRepository userRepository, JwtService jwtService, MyUserDetailService userDetailsService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String authHeader) {
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

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();

        UserDto dto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsernameForController(),
                user.getAvatarUrl(),
                user.getAvatarColor(),
                user.getAvatarInitials(),
                user.getFriends().stream()
                        .map(friend -> new FriendDto(
                                friend.getId(),
                                friend.getEmail(),
                                friend.getAvatarInitials(),
                                friend.getAvatarColor(),
                                friend.getAvatarUrl(),
                                friend.getUsernameForController(),
                                friend.getDescription()
                        ))
                        .collect(Collectors.toSet()),
                user.getDescription()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {

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

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();

        String newUsername = requestBody.get("username");
        if (newUsername == null || newUsername.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        }

        user.setUsername(newUsername.trim());
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Username updated successfully",
                "username", user.getUsernameForController()
        ));
    }

    @PutMapping("/description")
    public ResponseEntity<?> updateDescription(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {

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

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();

        String newDescription = requestBody.get("description");
        if (newDescription == null) {
            return ResponseEntity.badRequest().body("Description is missing");
        }

        user.setDescription(newDescription.trim());
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Description updated successfully",
                "description", user.getDescription()
        ));
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {

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

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();

        String avatarUrl = requestBody.get("avatarUrl");
        String avatarColor = requestBody.get("avatarColor");
        String avatarInitials = requestBody.get("avatarInitials");

        if (avatarUrl != null) {
            user.setAvatarUrl(avatarUrl.trim());
        }
        if (avatarColor != null) {
            user.setAvatarColor(avatarColor.trim());
        }
        if (avatarInitials != null) {
            user.setAvatarInitials(avatarInitials.trim());
        }

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Avatar updated successfully",
                "avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : "",
                "avatarColor", user.getAvatarColor() != null ? user.getAvatarColor() : "",
                "avatarInitials", user.getAvatarInitials() != null ? user.getAvatarInitials() : ""
        ));
    }
}