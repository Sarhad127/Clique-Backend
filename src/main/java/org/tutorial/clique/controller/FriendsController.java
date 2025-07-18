package org.tutorial.clique.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.tutorial.clique.configuration.MyUserDetailService;
import org.tutorial.clique.model.User;
import org.tutorial.clique.repository.UserRepository;
import org.tutorial.clique.service.JwtService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/friends")
@CrossOrigin(origins = "http://localhost:3000")
public class FriendsController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MyUserDetailService myUserDetailService;
    private final SimpMessagingTemplate messagingTemplate;

    public FriendsController(UserRepository userRepository,
                             JwtService jwtService,
                             MyUserDetailService myUserDetailService,
                             SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.myUserDetailService = myUserDetailService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFriend(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("identifier") String identifier
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
        }

        String token = authHeader.substring(7);
        String requesterEmail;
        try {
            requesterEmail = jwtService.extractUsername(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        UserDetails userDetails = myUserDetailService.loadUserByUsername(requesterEmail);
        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed.");
        }

        Optional<User> requesterOpt = userRepository.findByEmail(requesterEmail);
        if (requesterOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requesting user not found.");
        }

        Optional<User> friendOpt = userRepository.findByUsername(identifier);
        if (friendOpt.isEmpty()) {
            friendOpt = userRepository.findByEmail(identifier);
        }

        if (friendOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User requester = requesterOpt.get();
        User friend = friendOpt.get();

        if (requester.getId().equals(friend.getId())) {
            return ResponseEntity.badRequest().body("Cannot add yourself as a friend.");
        }

        if (requester.getFriends().contains(friend)) {
            return ResponseEntity.badRequest().body("User is already your friend.");
        }

        requester.getFriends().add(friend);
        friend.getFriends().add(requester);
        userRepository.save(requester);
        userRepository.save(friend);

        String notificationMessage = requester.getUsername() + " sent you a friend request.";
        messagingTemplate.convertAndSend("/server/friendRequest/" + friend.getUsername(), notificationMessage);


        return ResponseEntity.ok("Friend added successfully.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFriend(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("identifier") String identifier
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
        }

        String token = authHeader.substring(7);
        String requesterEmail;
        try {
            requesterEmail = jwtService.extractUsername(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        UserDetails userDetails = myUserDetailService.loadUserByUsername(requesterEmail);
        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed.");
        }

        Optional<User> requesterOpt = userRepository.findByEmail(requesterEmail);
        if (requesterOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requesting user not found.");
        }

        Optional<User> friendOpt = userRepository.findByUsername(identifier);
        if (friendOpt.isEmpty()) {
            friendOpt = userRepository.findByEmail(identifier);
        }

        if (friendOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User requester = requesterOpt.get();
        User friend = friendOpt.get();

        if (!requester.getFriends().contains(friend)) {
            return ResponseEntity.badRequest().body("User is not in your friend list.");
        }

        requester.getFriends().remove(friend);
        friend.getFriends().remove(requester);
        userRepository.save(requester);
        userRepository.save(friend);

        String notificationMessage = requester.getUsername() + " removed you from their friends list.";
        messagingTemplate.convertAndSend("/server/friendRemoved/" + friend.getUsername(), notificationMessage);

        return ResponseEntity.ok("Friend removed successfully.");
    }
}