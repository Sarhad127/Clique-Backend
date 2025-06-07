package org.tutorial.clique.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tutorial.clique.model.Chat;
import org.tutorial.clique.model.Message;
import org.tutorial.clique.model.MessageStatus;
import org.tutorial.clique.model.User;
import org.tutorial.clique.repository.ChatRepository;
import org.tutorial.clique.repository.MessageRepository;
import org.tutorial.clique.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    public List<Message> getMessages(Long senderId, Long receiverId) {
        return messageRepository.findBySenderAndReceiver(senderId, receiverId);
    }

    public Message sendMessage(Long senderId, Long receiverId, Long chatId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        Message newMessage = new Message(sender, receiver, content, LocalDateTime.now(), MessageStatus.SENT);
        newMessage.setChat(chat);
        return messageRepository.save(newMessage);
    }
}
