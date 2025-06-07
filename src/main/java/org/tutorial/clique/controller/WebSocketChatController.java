package org.tutorial.clique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.tutorial.clique.dto.MessageDto;
import org.tutorial.clique.model.Message;
import org.tutorial.clique.model.MessageStatus;
import org.tutorial.clique.model.User;
import org.tutorial.clique.repository.UserRepository;
import org.tutorial.clique.service.MessageService;

import java.time.LocalDateTime;

@Controller
public class WebSocketChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageDto messageDto) {
        try {
            User sender = userRepository.findById(messageDto.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            User receiver = userRepository.findById(messageDto.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
            Message message = new Message(sender, receiver, messageDto.getContent(), LocalDateTime.now(), MessageStatus.SENT);
            messageService.sendMessage(sender.getId(), receiver.getId(), messageDto.getContent());
            MessageDto outgoing = MessageDto.fromEntity(message);
            messagingTemplate.convertAndSend("/topic/messages/" + receiver.getId(), outgoing);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}