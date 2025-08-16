package com.ycyw.demo.controllers;

import com.ycyw.demo.models.Message;
import com.ycyw.demo.models.Conversation;
import com.ycyw.demo.models.User;
import com.ycyw.demo.services.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.ChatGrant;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.apiKey}")
    private String apiKey;

    @Value("${twilio.apiSecret}")
    private String apiSecret;

    @Value("${twilio.chatServiceSid}")
    private String chatServiceSid;

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/user")
    public User createUser(@RequestParam String firstname,
                           @RequestParam String lastname,
                           @RequestParam String email) {
        return chatService.createUser(firstname, lastname, email);
    }

    @GetMapping("/conversation/{userId}/{name}")
    public Conversation getOrCreateConversation(@PathVariable Long userId,
                                                @PathVariable String name) {
        return chatService.getOrCreateConversation(userId, name);
    }

    @GetMapping("/messages/{conversationId}")
    public List<Message> getMessages(@PathVariable Long conversationId) {
        return chatService.getMessages(conversationId);
    }

    @PostMapping("/messages/{conversationId}")
    public Message sendMessage(@PathVariable Long conversationId,
                               @RequestParam String senderType,
                               @RequestParam String body) {
        return chatService.sendMessage(conversationId, senderType, body);
    }


    @GetMapping("/token/{identity}")
    public String getToken(@PathVariable String identity) {
        ChatGrant chatGrant = new ChatGrant();
        chatGrant.setServiceSid(chatServiceSid);

        AccessToken token = new AccessToken.Builder(
                accountSid,   // AC...
                apiKey,       // SK...
                apiSecret  // secret du SK...

        )
                .identity(identity)
                .grant(chatGrant)
                .build();

        return token.toJwt();
    }


}
