package com.ycyw.demo.services;

import com.ycyw.demo.models.User;
import com.ycyw.demo.models.Conversation;
import com.ycyw.demo.models.Message;

import com.ycyw.demo.repositories.UserRepository;
import com.ycyw.demo.repositories.ConversationRepository;
import com.ycyw.demo.repositories.MessageRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatService {

    private final UserRepository userRepo;
    private final ConversationRepository convRepo;
    private final MessageRepository msgRepo;

    public ChatService(UserRepository userRepo, ConversationRepository convRepo, MessageRepository msgRepo) {
        this.userRepo = userRepo;
        this.convRepo = convRepo;
        this.msgRepo = msgRepo;
    }

    // Récupère ou crée une conversation pour un user
    public Conversation getOrCreateConversation(Long userId, String name) {
        return convRepo.findByUserIdAndName(userId, name)
                .orElseGet(() -> {
                    User user = userRepo.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Conversation conv = new Conversation();
                    conv.setName(name);
                    conv.setUser(user);
                    return convRepo.save(conv);
                });
    }

    // Récupère tous les messages d'une conversation
    public List<Message> getMessages(Long conversationId) {
        Conversation conv = convRepo.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        return msgRepo.findByConversationOrderByCreatedAtAsc(conv);
    }

    // Envoie un message dans une conversation
    public Message sendMessage(Long conversationId, String senderType, String body) {
        Conversation conv = convRepo.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        Message msg = new Message();
        msg.setConversation(conv);
        msg.setSenderType(senderType);
        msg.setBody(body);
        return msgRepo.save(msg);
    }

    // Crée un utilisateur pour les tests
    public User createUser(String firstname, String lastname, String email) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        return userRepo.save(user);
    }
}
