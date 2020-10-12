package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserChangePasswordService {
    private final UserRepository userRepository;

    public UserChangePasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<MessageResponse> checkOldPasswordValidity(String userEmail, String userOldPassword) {
        //User always exists cause its logged in!
        User user = userRepository.findByEmailIgnoreCase(userEmail);
        if (!BCrypt.checkpw(userOldPassword, user.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The old password which u entered dont match"));
        }
        return ResponseEntity.ok(new MessageResponse("Password Matches........"));
    }
}
// TODO change password
//    refactor auth controller


