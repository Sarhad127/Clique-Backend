package org.tutorial.clique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.clique.dto.MessageDto;
import org.tutorial.clique.model.Chat;
import org.tutorial.clique.model.Message;
import org.tutorial.clique.model.User;
import org.tutorial.clique.service.ChatService;
import org.tutorial.clique.service.MessageService;
import org.tutorial.clique.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<MessageDto>> getMessages(
            @RequestParam("userId") Long userId,
            @RequestParam("friendId") Long friendId) {
        List<Message> messages = messageService.getMessages(userId, friendId);
        List<MessageDto> messageDtos = MessageDto.fromEntities(messages);
        return ResponseEntity.ok(messageDtos);
    }

    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto messageDto) {
        try {
            User sender = userRepository.findById(messageDto.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            User receiver = userRepository.findById(messageDto.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
            Chat chat = chatService.getOrCreateChat(sender.getId(), receiver.getId());
            Message savedMessage = messageService.sendMessage(sender.getId(), receiver.getId(), chat.getId(), messageDto.getContent());
            MessageDto newMessageDto = MessageDto.fromEntity(savedMessage);
            return new ResponseEntity<>(newMessageDto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/group")
    public ResponseEntity<List<MessageDto>> getGroupMessages(@RequestParam("groupId") Long groupId) {
        try {
            List<Message> messages = messageService.getMessagesByGroupId(groupId);
            List<MessageDto> messageDtos = MessageDto.fromEntities(messages);
            return ResponseEntity.ok(messageDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
