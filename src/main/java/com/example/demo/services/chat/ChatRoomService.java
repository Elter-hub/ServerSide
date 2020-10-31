package com.example.demo.services.chat;

import com.example.demo.models.chat.ChatRoom;
import com.example.demo.repository.chat.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    @Autowired private ChatRoomRepository chatRoomRepository;

    public String getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .orElseGet(() -> {
                    if(!createIfNotExist) {
                        return  "";
                    }
                    String  chatId =
                            String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return chatId;
                });
    }
}

