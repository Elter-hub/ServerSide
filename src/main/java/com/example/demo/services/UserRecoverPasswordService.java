package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.EmailConfirmationToken;
import com.example.demo.models.User;
import com.example.demo.repository.EmailConfirmationTokenRepository;
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
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    public UserRecoverPasswordService(UserRepository userRepository,
                                      EmailSenderService emailSenderService,
                                      PasswordEncoder passwordEncoder,
                                      EmailConfirmationTokenRepository emailConfirmationTokenRepository) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
    }

    public ResponseEntity<MessageResponse> forgotPassword(String email) {
        System.out.println("FOrgotPassword method starts");
        Optional<User> userOptional = Optional
                .ofNullable(userRepository.findByEmailIgnoreCase(email));
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email doesnt exist!"));
        }
        System.out.println("After checking user");

        Optional<EmailConfirmationToken> confirmationTokenOptional = Optional
                .ofNullable(emailConfirmationTokenRepository.findByEmailConfirmationToken(email));
        if (!confirmationTokenOptional.isPresent()) {
            return ResponseEntity.ok(new MessageResponse("Invalid E Token"));
        }
        System.out.println("After checking emailConfirmationToken");


        User user = userOptional.get();
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(user);
        String tokenForPasswordRecover = generateToken();
        emailConfirmationToken.setEmailConfirmationToken(tokenForPasswordRecover);
        emailConfirmationToken.setEmailConfirmationTokenCreatedDate(LocalDateTime.now());
        System.out.println("TOken for recover " + emailConfirmationToken.getEmailConfirmationToken());
        emailConfirmationTokenRepository.save(emailConfirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Password recovering ");
        mailMessage.setFrom("ihor04@gmail.com");
        mailMessage.setText("To recover password please click in the link below  : "
                + "http://localhost:4200/forgot-password/change-password?token=" + tokenForPasswordRecover);

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

//    public ResponseEntity<MessageResponse> resetPassword(String token, String password) {
//        System.out.println("Reset password " + token + " " + password);
//
//        Optional<User> userOptional = Optional
//                .ofNullable(userRepository.findByTokenForRecover(token));
//
//        if (!userOptional.isPresent()) {
//            System.out.println("Reset Optional " + userOptional.get());
//            return ResponseEntity.ok(new MessageResponse("Invalid Tokenss"));
//        }
//        Optional<EmailConfirmationToken> confirmationTokenOptional = Optional
//                .ofNullable(emailConfirmationTokenRepository.findByTokenForRecover(token));
//
//        if (!confirmationTokenOptional.isPresent()) {
//            return ResponseEntity.ok(new MessageResponse("Invalid E Token"));
//        }
//
//        LocalDateTime tokenCreationDate = confirmationTokenOptional.get().getConfirmationTokenCreatedDate();
//
//        if (isTokenExpired(tokenCreationDate)) {
//            System.out.println("Is token expired  ");
//            return ResponseEntity.ok(new MessageResponse("Token Expired"));
//        }
//
//        User user = userOptional.get();
//        EmailConfirmationToken emailConfirmationToken = confirmationTokenOptional.get();
//
//        user.setPassword(passwordEncoder.encode(password));
//        emailConfirmationToken.setConfirmationToken(null);
//        emailConfirmationToken.setConfirmationTokenCreatedDate(null);
//        userRepository.save(user);
//        System.out.println("" +
//                "4545" +
//                "545");
//        System.out.println(user.toString());
//
//        return ResponseEntity.ok(new MessageResponse("Your password successfully updated."));
//    }

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
