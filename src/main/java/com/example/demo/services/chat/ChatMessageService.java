package com.example.demo.services.chat;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.chat.ChatMessage;
import com.example.demo.models.enums.EMessageStatus;
import com.example.demo.repository.chat.ChatMessageRepository;
import lombok.var;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomService chatRoomService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(EMessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(String senderId, String recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, EMessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        String chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatId(chatId).orElse(new ArrayList<>());

        if (chatMessages.size() > 0) {
            updateStatuses(senderId, recipientId, EMessageStatus.DELIVERED);
        }

        return chatMessages;
    }

    public ChatMessage findById(String id) {
        return chatMessageRepository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(EMessageStatus.DELIVERED);
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(String senderId, String recipientId, EMessageStatus status) {
        ChatMessage sendedMessage = chatMessageRepository.findBySenderId(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender doesnt exist"));
        ChatMessage receivedMessage = chatMessageRepository.findByRecipientId(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient doesnt exist"));

        sendedMessage.setStatus(EMessageStatus.DELIVERED);
        receivedMessage.setStatus(EMessageStatus.RECEIVED);

        chatMessageRepository.save(sendedMessage);
        chatMessageRepository.save(receivedMessage);

    }
}
