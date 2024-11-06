package com.example.chat_backend.controller;

import com.example.chat_backend.model.ChatMessage;
import com.example.chat_backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Controller
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void init() {
        // Envoyer les anciens messages aux nouveaux clients
        List<ChatMessage> messages = chatService.getMessages();
        for (ChatMessage message : messages) {
            messagingTemplate.convertAndSend("/topic/messages", message);
        }
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        ChatMessage sanitizedMessage = new ChatMessage(HtmlUtils.htmlEscape(message.getContent()), HtmlUtils.htmlEscape(message.getUser()));
        chatService.addMessage(sanitizedMessage);
        return sanitizedMessage;
    }

    @GetMapping("/messages")
    public List<ChatMessage> getMessages() {
        return chatService.getMessages();
    }

    @PostMapping("/message")
    public void sendMessageViaHttp(@RequestBody ChatMessage message) {
        ChatMessage sanitizedMessage = new ChatMessage(HtmlUtils.htmlEscape(message.getContent()), HtmlUtils.htmlEscape(message.getUser()));
        chatService.addMessage(sanitizedMessage);
        messagingTemplate.convertAndSend("/topic/messages", sanitizedMessage);
    }
}