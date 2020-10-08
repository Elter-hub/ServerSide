package com.example.demo.controllers;

import com.example.demo.dto.request.PasswordRecoverRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.services.UserRecoverPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api")
@RestController
public class PasswordRecoverController {

    private final UserRecoverPasswordService userRecoverPasswordService;

    public PasswordRecoverController(UserRecoverPasswordService userRecoverPasswordService) {
        this.userRecoverPasswordService = userRecoverPasswordService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody PasswordRecoverRequest email) {
        System.out.println("forgot password from passController");
        return userRecoverPasswordService.forgotPassword(email.getEmailForRecoveringPassword());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody PasswordRecoverRequest passwordRecoverRequest) {
        return userRecoverPasswordService.resetPassword(passwordRecoverRequest.getTokenForRecoveringPassword(),
                                                        passwordRecoverRequest.getPassword(),
                                                        passwordRecoverRequest.getEmailForRecoveringPassword());
    }
}
