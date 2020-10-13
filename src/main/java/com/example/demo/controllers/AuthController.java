package com.example.demo.controllers;

import javax.validation.Valid;

import com.example.demo.dto.request.EmailConfirmationRequest;
import com.example.demo.models.*;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.JwtResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.repository.EmailConfirmationTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TokenActionsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.auth.LoginService;
import com.example.demo.services.EmailSenderService;
import com.example.demo.services.auth.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final LoginService loginService;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final RegisterService registerService;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder encoder, LoginService loginService, EmailConfirmationTokenRepository emailConfirmationTokenRepository, RegisterService registerService, EmailSenderService emailSenderService, TokenActionsRepository tokenActionsRepository) {
        this.userRepository = userRepository;
        this.loginService = loginService;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.registerService = registerService;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return loginService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Create new user's account
        return registerService.registerUser(signUpRequest);
    }

    @PostMapping("/confirm")
    public ResponseEntity<MessageResponse> confirmUserAccount(@RequestBody EmailConfirmationRequest requestEmailToken) {
        EmailConfirmationToken token = emailConfirmationTokenRepository
                .findByEmailConfirmationToken(requestEmailToken.getEmailConfirmationToken());
        if (token != null) {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Email successfully confirmed!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Email problem"));
    }
}
