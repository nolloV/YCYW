package com.example.chat_backend.model;

public class ChatMessage {

    private String content;
    private String user;

    public ChatMessage() {
    }

    public ChatMessage(String content, String user) {
        this.content = content;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
