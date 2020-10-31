package com.example.demo.repository.chat;

import com.example.demo.models.chat.ChatMessage;
import com.example.demo.models.enums.EMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, EMessageStatus status);

    Optional<List<ChatMessage>> findByChatId(String chatId);

    Optional<ChatMessage> findBySenderId(String senderId);
    Optional<ChatMessage> findByRecipientId(String recipientId);
}
