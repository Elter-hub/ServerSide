package com.example.demo.services;

import com.example.demo.dto.response.UserMessagesResponse;
import com.example.demo.models.Message;
import com.example.demo.models.User;
import com.example.demo.models.enums.EMessage;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.List;

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
        Message newMessageUser = new Message(subject, message, EMessage.SEND, userEmail);
        Message newMessageAdmin = new Message(subject, message, EMessage.RECEIVE, userEmail);

        return saveMessages(user, admin, newMessageUser, newMessageAdmin);
    }

    public ResponseEntity<UserMessagesResponse> getMessages(String email){
        User user = this.userRepository.findByEmailIgnoreCase(email);
        List<Message> messages = user.getMessages();

        return ResponseEntity.ok(new UserMessagesResponse(messages));
    }

    public ResponseEntity<UserMessagesResponse> userResponseMessage(String userEmail, String subject, String message, @NotBlank Long messageId) {
        User user = this.userRepository.findByEmailIgnoreCase(userEmail);
        User admin = this.userRepository.findAdmin();

        Message newMessageUser = new Message(subject, message, EMessage.RECEIVE, admin.getEmail(), userEmail);
        Message newMessageAdmin = new Message(subject, message, EMessage.SEND, admin.getEmail(), userEmail);
//        admin ↘↘↘↘↘↘
        Message respondedMessage = this.messageRepository.findByMessageId(messageId);
        respondedMessage.setResponded(true);
        this.messageRepository.save(respondedMessage);
        return saveMessages(user, admin, newMessageUser, newMessageAdmin);
    }

    public ResponseEntity<UserMessagesResponse> saveMessages(User user, User admin, Message newMessageUser, Message newMessageAdmin) {
        this.messageRepository.save(newMessageUser);
        this.messageRepository.save(newMessageAdmin);

        user.getMessages().add(newMessageUser);
        admin.getMessages().add(newMessageAdmin);

        this.userRepository.save(user);
        this.userRepository.save(admin);

        return ResponseEntity.ok(new UserMessagesResponse(user.getMessages()));
    }
}
