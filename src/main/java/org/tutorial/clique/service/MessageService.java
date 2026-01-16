package org.tutorial.clique.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tutorial.clique.model.*;
import org.tutorial.clique.repository.ChatRepository;
import org.tutorial.clique.repository.GroupRepository;
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

    @Autowired
    private GroupRepository groupRepository;

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

    public List<Message> getMessagesByGroupId(Long groupId) {
        return messageRepository.findByGroupIdOrderByTimestampAsc(groupId);
    }

    public Message sendGroupMessage(Long senderId, Long groupId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setContent(content);
        newMessage.setTimestamp(LocalDateTime.now());
        newMessage.setStatus(MessageStatus.SENT);
        newMessage.setReceiver(null);
        newMessage.setChat(null);
        newMessage.setGroup(group);

        return messageRepository.save(newMessage);
    }
}
