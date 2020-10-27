package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.dto.response.UserMessagesResponse;
import com.example.demo.models.Message;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.enums.EMessage;
import com.example.demo.models.enums.ERole;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserMessagesService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;


    public UserMessagesService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public ResponseEntity<UserMessagesResponse> userSendMessage(String userEmail, String subject, String message){
        User user = this.userRepository.findByEmailIgnoreCase(userEmail);
        User admin = this.userRepository.findAdmin();
        Message newMessageUser = new Message(subject, message, EMessage.SEND);
        Message newMessageAdmin = new Message(subject, message, EMessage.RECEIVE);

        this.messageRepository.save(newMessageUser);
        this.messageRepository.save(newMessageAdmin);
        user.getMessages().add(newMessageUser);
        admin.getMessages().add(newMessageAdmin);
        this.userRepository.save(user);
        this.userRepository.save(admin);
        return ResponseEntity.ok(new UserMessagesResponse(user.getMessages()));
    }

    public ResponseEntity<UserMessagesResponse> getMessages(String email){
        User user = this.userRepository.findByEmailIgnoreCase(email);
        List<Message> messages = user.getMessages();

        return ResponseEntity.ok(new UserMessagesResponse(messages));
    }

//    public ResponseEntity<UserMessagesResponse> responseMessage(String userEmail, String subject, String message){
//
//    }
}
