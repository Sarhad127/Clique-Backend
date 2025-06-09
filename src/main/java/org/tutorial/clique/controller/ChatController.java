package org.tutorial.clique.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.clique.dto.ChatDto;
import org.tutorial.clique.model.Chat;
import org.tutorial.clique.repository.UserRepository;
import org.tutorial.clique.service.ChatService;
import org.tutorial.clique.service.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public ChatController(ChatService chatService, UserRepository userRepository, JwtService jwtService) {
        this.chatService = chatService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/start")
    public ResponseEntity<ChatDto> startChat(@RequestParam Long friendId, @RequestHeader("Authorization") String authHeader) {
        Long currentUserId = getUserIdFromAuthHeader(authHeader);
        Chat chat = chatService.getOrCreateChat(currentUserId, friendId);
        return ResponseEntity.ok(ChatDto.fromEntity(chat));
    }

    @GetMapping
    public ResponseEntity<List<ChatDto>> getUserChats(@RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuthHeader(authHeader);
        List<Chat> chats = chatService.getChatsForUser(userId);
        return ResponseEntity.ok(ChatDto.fromEntities(chats));
    }

    private Long getUserIdFromAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(
            @PathVariable Long chatId,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = getUserIdFromAuthHeader(authHeader);
        chatService.deleteChatForUser(chatId, userId);
        return ResponseEntity.noContent().build();
    }
}
