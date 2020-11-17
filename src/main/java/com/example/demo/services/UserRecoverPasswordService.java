package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.PasswordRecoverToken;
import com.example.demo.models.User;
import com.example.demo.repository.PasswordRecoverTokenRepository;
import com.example.demo.repository.TokenActionsRepository;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface UserRecoverPasswordService {
    ResponseEntity<MessageResponse> forgotPassword(String email);
    ResponseEntity<MessageResponse> resetPassword(String token, String password, String emailForRecoveringPassword);
    ResponseEntity<MessageResponse> userResetPassword(String email, String newPassword);
    ResponseEntity<MessageResponse> confirmPasswordChanges(String email, String token);
    boolean isTokenExpired(final LocalDateTime tokenCreationDate);
    PasswordRecoverToken tokenActions(User user, TokenActionsRepository tokenActionsRepository,
                                             PasswordRecoverTokenRepository passwordRecoverTokenRepository);
}
