package com.example.demo.controllers;

import com.example.demo.dto.request.PasswordRecoverRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.services.UserRecoverPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api")
@RestController
public class PasswordRecoverController {

    private final UserRecoverPasswordService userRecoverPasswordService;

    public PasswordRecoverController(UserRecoverPasswordService userRecoverPasswordService) {
        this.userRecoverPasswordService = userRecoverPasswordService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody PasswordRecoverRequest email) {
        return userRecoverPasswordService.forgotPassword(email.getEmail());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody PasswordRecoverRequest password) {
        System.out.println("Reset is invoked");
        return userRecoverPasswordService.resetPassword(password.getTokenForRecoveringPassword(), password.getPassword());
    }
}
