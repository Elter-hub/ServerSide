package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.PasswordRecoverToken;
import com.example.demo.models.TokenActions;
import com.example.demo.models.User;
import com.example.demo.repository.PasswordRecoverTokenRepository;
import com.example.demo.repository.TokenActionsRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserChangePasswordService {
    private final UserRepository userRepository;
    private final UserRecoverPasswordService userRecoverPasswordService;
    private final PasswordRecoverTokenRepository passwordRecoverTokenRepository;
    private final TokenActionsRepository tokenActionsRepository;
    private final EmailSenderService emailSenderService;

    public UserChangePasswordService(UserRepository userRepository, UserRecoverPasswordService userRecoverPasswordService, PasswordRecoverTokenRepository passwordRecoverTokenRepository, TokenActionsRepository tokenActionsRepository, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.userRecoverPasswordService = userRecoverPasswordService;
        this.passwordRecoverTokenRepository = passwordRecoverTokenRepository;
        this.tokenActionsRepository = tokenActionsRepository;
        this.emailSenderService = emailSenderService;
    }

    public ResponseEntity<MessageResponse> checkOldPasswordValidity(String userEmail, String userOldPassword) {
        //User always exists cause its logged in!
        User user = userRepository.findByEmailIgnoreCase(userEmail);
        if (!BCrypt.checkpw(userOldPassword, user.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The old password which u entered dont match"));
        }
        return forgotPassword(userEmail);
    }

    public ResponseEntity<MessageResponse> forgotPassword(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        Optional<PasswordRecoverToken> tokenByEmailOptional = passwordRecoverTokenRepository.findByUserEmailForPasswordRecovering(user.getEmail());
        // Dont send new token if is present and less than 30min alive
        if (tokenByEmailOptional.isPresent() && !userRecoverPasswordService.isTokenExpired(tokenByEmailOptional.get().getPasswordConfirmationTokenCreatedDate())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("PLease follow the link in the email we send you"));
        }

        //Additional entity for convenience 
        PasswordRecoverToken passwordRecoverToken = userRecoverPasswordService.tokenActions(user,
                tokenActionsRepository, passwordRecoverTokenRepository);
        emailSenderService.sendEmail(user.getEmail(), "Password recovering ",
                "To recover password please click in the link below  : "
                        + "http://localhost:4200/user-change-password?token=" + passwordRecoverToken.getPasswordRecoverToken() +
                        "&email=" + user.getEmail());
        ;

        return ResponseEntity.ok(new MessageResponse("Check your email for further actions"));
    }
}


