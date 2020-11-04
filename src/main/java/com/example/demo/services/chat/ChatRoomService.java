package com.example.demo.services.chat;

import com.example.demo.models.chat.ChatRoom;
import com.example.demo.repository.chat.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        Optional<String> chatId = chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId);

        if (chatId.isPresent()) {
            return chatId;
        } else {
            if (!createIfNotExist) {
                return Optional.empty();
            }

            String newChatId = String.format("%s%s", senderId, recipientId);

            ChatRoom senderRecipient = ChatRoom
                    .builder()
                    .chatId(newChatId)
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .build();

            ChatRoom recipientSender = ChatRoom
                    .builder()
                    .chatId(newChatId)
                    .senderId(recipientId)
                    .recipientId(senderId)
                    .build();
            chatRoomRepository.save(senderRecipient);
            chatRoomRepository.save(recipientSender);

            return Optional.of(newChatId);
        }
    }
}
