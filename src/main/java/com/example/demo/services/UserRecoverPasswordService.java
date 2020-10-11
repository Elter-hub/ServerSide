package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.PasswordRecoverToken;
import com.example.demo.models.TokenActions;
import com.example.demo.models.User;
import com.example.demo.repository.PasswordRecoverTokenRepository;
import com.example.demo.repository.TokenActionsRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRecoverPasswordService {

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordRecoverTokenRepository passwordRecoverTokenRepository;
    private final TokenActionsRepository tokenActionsRepository;

    public UserRecoverPasswordService(UserRepository userRepository,
                                      EmailSenderService emailSenderService,
                                      PasswordEncoder passwordEncoder,
                                      PasswordRecoverTokenRepository passwordRecoverTokenRepository, TokenActionsRepository tokenActionsRepository) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.passwordRecoverTokenRepository = passwordRecoverTokenRepository;
        this.tokenActionsRepository = tokenActionsRepository;
    }

    public ResponseEntity<MessageResponse> forgotPassword(String email) {
        Optional<User> userOptional = Optional
                .ofNullable(userRepository.findByEmailIgnoreCase(email));
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Email doesnt exist!"));
        }
        User user = userOptional.get();
        Optional<TokenActions> tokenActionsByUserIdOptional = tokenActionsRepository.findByUserId(user.getId());


        PasswordRecoverToken passwordRecoverToken = new PasswordRecoverToken(user);
        String tokenForPasswordRecover = generateToken();
        passwordRecoverToken.setPasswordRecoverToken(tokenForPasswordRecover);
        passwordRecoverToken.setPasswordConfirmationTokenCreatedDate(LocalDateTime.now());
        passwordRecoverToken.setUserEmailForPasswordRecovering(user.getEmail());
        passwordRecoverTokenRepository.save(passwordRecoverToken);

        if (tokenActionsByUserIdOptional.isPresent()){
            tokenActionsByUserIdOptional.get().setPasswordRecoverToken(tokenForPasswordRecover);
        tokenActionsRepository.save(tokenActionsByUserIdOptional.get());
        }else {
            tokenActionsRepository.save(new TokenActions(passwordRecoverToken, user));
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Password recovering ");
        mailMessage.setFrom("ihor04@gmail.com");
        mailMessage.setText("To recover password please click in the link below  : "
                + "http://localhost:4200/change-password?token=" + tokenForPasswordRecover
                + "&email=" + user.getEmail());

        emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.ok(new MessageResponse("Check your email for further actions"));
    }

    /**
     * Generate unique token. You may add multiple parameters to create a strong
     * token.
     *
     * @return unique token
     */

    private String generateToken() {
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    public ResponseEntity<MessageResponse> resetPassword(String token, String password, String emailForRecoveringPassword) {
        Optional<User> userOptional = Optional
                .ofNullable(userRepository.findByEmailIgnoreCase(emailForRecoveringPassword));
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User with provided email doesnt exist!"));
        }

        Optional<PasswordRecoverToken> passwordTokenOptional = Optional
                .ofNullable(passwordRecoverTokenRepository.findByPasswordRecoverToken(token));
        if (!passwordTokenOptional.isPresent()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("You already changed password. Invalid Token"));
        }

        LocalDateTime tokenCreationDate = passwordTokenOptional.get().getPasswordConfirmationTokenCreatedDate();
        if (isTokenExpired(tokenCreationDate)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Token Expired"));
        }

        User user = userOptional.get();
        PasswordRecoverToken passwordRecoverToken = passwordTokenOptional.get();

        user.setPassword(passwordEncoder.encode(password));
        passwordRecoverToken.setPasswordRecoverToken(null);
        passwordRecoverToken.setPasswordConfirmationTokenCreatedDate(null);
        passwordRecoverToken.setUserEmailForPasswordRecovering(null);
        userRepository.save(user);
        System.out.println("User from reset " + user.toString());

        return ResponseEntity.ok(new MessageResponse("Your password successfully updated."));
    }

    /**
     * Check whether the created token expired or not.
     *
     * @param tokenCreationDate
     * @return true or false
     */
    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);
        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }
}
