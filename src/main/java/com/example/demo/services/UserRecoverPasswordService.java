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
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
        //Can be invoked by unauthorized user who forgot password and can enter any email...
        if (!userOptional.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email doesnt exist!"));
        }
        User user = userOptional.get();
        Optional<PasswordRecoverToken> tokenByEmailOptional = passwordRecoverTokenRepository.findByUserEmailForPasswordRecovering(user.getEmail());
        if (tokenByEmailOptional.isPresent() && !isTokenExpired(tokenByEmailOptional.get().getPasswordConfirmationTokenCreatedDate())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("PLease follow the link in the email we send you"));
        }
        PasswordRecoverToken passwordRecoverToken = tokenActions(user, tokenActionsRepository, passwordRecoverTokenRepository);
        emailSenderService.sendEmail(user.getEmail(), "Password recovering ",

                "To recover password please click visit the link below  : "
                        + "http://localhost:4200/change-password?token=" + passwordRecoverToken.getPasswordRecoverToken()
                        + "&email=" + user.getEmail() + "\n Or enter your token" + passwordRecoverToken.getPasswordRecoverToken());

        return ResponseEntity.ok(new MessageResponse("Check your email for further actions"));

    }

    @Transactional
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
        user.setPassword(passwordEncoder.encode(password));
        passwordRecoverTokenRepository.deleteByUserEmailForPasswordRecovering(user.getEmail());
        userRepository.save(user);
        System.out.println("User from reset " + user.toString());

        return ResponseEntity.ok(new MessageResponse("Your password successfully updated."));
    }


    public boolean isTokenExpired(final LocalDateTime tokenCreationDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);
        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

    public ResponseEntity<MessageResponse> userResetPassword(String email, String newPassword) {
        User user = userRepository.findByEmailIgnoreCase(email);
        user.setTemporalPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Successively set Temporal password "));
    }

    @Transactional
    public ResponseEntity<MessageResponse> confirmPasswordChanges(String email, String token) {
        User user = userRepository.findByEmailIgnoreCase(email);
        System.out.println("🧊");
        PasswordRecoverToken passwordRecoverToken = passwordRecoverTokenRepository.findByPasswordRecoverToken(token);
        if (passwordRecoverToken != null) {
            System.out.println("💥💥💥");
            user.setPassword(user.getTemporalPassword());
            passwordRecoverTokenRepository.deleteByUserEmailForPasswordRecovering(email);
            userRepository.save(user);
        }
        return ResponseEntity.ok(new MessageResponse("Password successfully changed!"));
    }

    public PasswordRecoverToken tokenActions(User user, TokenActionsRepository tokenActionsRepository, PasswordRecoverTokenRepository passwordRecoverTokenRepository) {
        Optional<TokenActions> tokenActionsByUserIdOptional = tokenActionsRepository.findByUserId(user.getId());
        PasswordRecoverToken passwordRecoverToken = new PasswordRecoverToken(user);
        passwordRecoverTokenRepository.save(passwordRecoverToken);
        if (tokenActionsByUserIdOptional.isPresent()) {
            tokenActionsByUserIdOptional.get().setPasswordRecoverToken(passwordRecoverToken.getPasswordRecoverToken());
            tokenActionsRepository.save(tokenActionsByUserIdOptional.get());
        } else {
            tokenActionsRepository.save(new TokenActions(passwordRecoverToken, user));
        }
        return passwordRecoverToken;
    }
}
