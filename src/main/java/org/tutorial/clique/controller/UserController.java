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
                                friend.getUsernameForController()
                        ))
                        .collect(Collectors.toSet()),
                user.getServers().stream()
                        .map(server -> server.getId())
                        .collect(Collectors.toSet())
        );

        return ResponseEntity.ok(dto);
    }
}