package com.example.demo.models.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ChatRoom {
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
