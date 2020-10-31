package com.example.demo.repository.chat;

import com.example.demo.models.chat.ChatMessage;
import com.example.demo.models.enums.EMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, EMessageStatus status);

    List<ChatMessage> findByChatId(String chatId);
}
