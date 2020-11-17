package com.example.demo.services.implementation;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.PasswordRecoverToken;
import com.example.demo.models.User;
import com.example.demo.repository.PasswordRecoverTokenRepository;
import com.example.demo.repository.TokenActionsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserChangePasswordService;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class UserChangePasswordServiceImpl implements UserChangePasswordService {
    private final UserRepository userRepository;
    private final UserRecoverPasswordServiceImpl userRecoverPasswordServiceImpl;
    private final PasswordRecoverTokenRepository passwordRecoverTokenRepository;
    private final TokenActionsRepository tokenActionsRepository;
    private final EmailSenderServiceImpl emailSenderServiceImpl;
    private final Environment environment;


    public UserChangePasswordServiceImpl(UserRepository userRepository, UserRecoverPasswordServiceImpl userRecoverPasswordServiceImpl, PasswordRecoverTokenRepository passwordRecoverTokenRepository, TokenActionsRepository tokenActionsRepository, EmailSenderServiceImpl emailSenderServiceImpl, Environment environment) {
        this.userRepository = userRepository;
        this.userRecoverPasswordServiceImpl = userRecoverPasswordServiceImpl;
        this.passwordRecoverTokenRepository = passwordRecoverTokenRepository;
        this.tokenActionsRepository = tokenActionsRepository;
        this.emailSenderServiceImpl = emailSenderServiceImpl;
        this.environment = environment;
    }

    @Override
    public ResponseEntity<MessageResponse> checkOldPasswordValidity(String userEmail, String userOldPassword) {
        User user = userRepository.findByEmailIgnoreCase(userEmail);
        if (!BCrypt.checkpw(userOldPassword, user.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The old password which u entered dont match"));
        }
        return forgotPassword(userEmail);
    }

    @Override
    public ResponseEntity<MessageResponse> forgotPassword(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        Optional<PasswordRecoverToken> tokenByEmailOptional = passwordRecoverTokenRepository.findByUserEmailForPasswordRecovering(user.getEmail());
        if (tokenByEmailOptional.isPresent() && !userRecoverPasswordServiceImpl.isTokenExpired(tokenByEmailOptional.get().getPasswordConfirmationTokenCreatedDate())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("PLease follow the link in the email we send you"));
        }

        PasswordRecoverToken passwordRecoverToken = userRecoverPasswordServiceImpl.tokenActions(user,
                tokenActionsRepository, passwordRecoverTokenRepository);
        emailSenderServiceImpl.sendEmail(user.getEmail(), "Password recovering ",
                environment.getProperty("app.recover.password") + passwordRecoverToken.getPasswordRecoverToken()
                        + "&email=" + user.getEmail()
                        + "\n or enter this token " + passwordRecoverToken.getPasswordRecoverToken());

        return ResponseEntity.ok(new MessageResponse("Check your email for further actions"));
    }
}


