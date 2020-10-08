package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.User;
import com.example.demo.repository.PasswordRecoverTokenRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserChangePasswordService {

    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordRecoverTokenRepository passwordRecoverTokenRepository;


    public UserChangePasswordService(UserRepository userRepository, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder, PasswordRecoverTokenRepository passwordRecoverTokenRepository) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.passwordRecoverTokenRepository = passwordRecoverTokenRepository;
    }

    public ResponseEntity<MessageResponse> checkOldPasswordValidity(String userEmail, String userOldPassword) {
        //User always exists cause its logged in!
        User user = userRepository.findByEmailIgnoreCase(userEmail);
        if (BCrypt.checkpw(userOldPassword, user.getPassword())){
            String tokenUserChangePassword = UUID.randomUUID().toString();
            tokenUserChangePassword.setPasswordRecoverToken(tokenForPasswordRecover);
            tokenUserChangePassword.setPasswordConfirmationTokenCreatedDate(LocalDateTime.now());
            tokenUserChangePassword.setUserEmailForPasswordRecovering(user.getEmail());
            passwordRecoverTokenRepository.save(passwordRecoverToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Password recovering ");
            mailMessage.setFrom("ihor04@gmail.com");
            mailMessage.setText("To recover password please click in the link below  : "
                    + "http://localhost:4200/change-password?token=" + tokenUserChangePassword);

            emailSenderService.sendEmail(mailMessage);
        }else {
            return ResponseEntity.ok(new MessageResponse("The old password which u entered dont match"));
        }


        return ResponseEntity.ok(new MessageResponse("The old password which u entered dont match"));
    }
}

