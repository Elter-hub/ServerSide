package com.example.demo.services.chat;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.chat.ChatMessage;
import com.example.demo.models.enums.EMessageStatus;
import com.example.demo.repository.chat.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private ChatRoomService chatRoomService;

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
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);


        List<ChatMessage> chatMessages = chatId.map(cId -> chatMessageRepository.findByChatId(cId)).orElse(new ArrayList<>());

        if(chatMessages.size() > 0) {
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
        ChatMessage message = chatMessageRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        message.setStatus(status);
        chatMessageRepository.save(message);
    }}
