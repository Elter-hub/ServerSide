package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface UserChangePasswordService {
    ResponseEntity<MessageResponse> checkOldPasswordValidity(String userEmail, String userOldPassword);
    ResponseEntity<MessageResponse> forgotPassword(String email);
}
