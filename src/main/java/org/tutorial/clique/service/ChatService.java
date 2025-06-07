package org.tutorial.clique.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tutorial.clique.model.Chat;
import org.tutorial.clique.model.User;
import org.tutorial.clique.repository.ChatRepository;
import org.tutorial.clique.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired private UserRepository userRepository;

    public List<Chat> getChatsForUser(Long userId) {
        return chatRepository.findAllByUserId(userId);
    }

    public Chat getOrCreateChat(Long user1Id, Long user2Id) {
        Optional<Chat> chatOpt = chatRepository.findChatBetweenUsers(user1Id, user2Id);
        if (chatOpt.isPresent()) {
            return chatOpt.get();
        }
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User1 not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User2 not found"));
        Chat newChat = new Chat();
        newChat.getParticipants().add(user1);
        newChat.getParticipants().add(user2);
        return chatRepository.save(newChat);
    }
}