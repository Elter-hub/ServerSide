package com.example.demo.models.chat;

import com.example.demo.models.enums.EMessageStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatMessage {
   @Id
   private String id;
   private String chatId;
   private String senderId;
   private String recipientId;
   private String senderName;
   private String recipientName;
   private String content;
   private LocalDateTime timestamp;
   private EMessageStatus status;
}
