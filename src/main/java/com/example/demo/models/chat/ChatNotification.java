package com.example.demo.models.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatNotification {
    private String id;
    private String senderId;
    private String senderName;
}
