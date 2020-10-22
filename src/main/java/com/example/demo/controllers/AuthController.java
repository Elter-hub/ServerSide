package com.example.demo.controllers;

import javax.validation.Valid;

import com.example.demo.dto.request.EmailConfirmationRequest;
import com.example.demo.dto.request.RefreshTokenRequest;
import com.example.demo.dto.response.RefreshJwtResponse;
import com.example.demo.models.*;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.JwtResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.repository.EmailConfirmationTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.auth.LoginService;
import com.example.demo.services.auth.RefreshTokenService;
import com.example.demo.services.auth.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final LoginService loginService;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final RegisterService registerService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserRepository userRepository, LoginService loginService,
                          EmailConfirmationTokenRepository emailConfirmationTokenRepository,
                          RegisterService registerService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.loginService = loginService;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.registerService = registerService;
        this.refreshTokenService = refreshTokenService;
    }

    // 2 queries
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return loginService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
    }
        // 7 Hibernate queries 🧰
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return registerService.registerUser(signUpRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshJwtResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken(),
                refreshTokenRequest.getUserEmail())

        ? refreshTokenService.generateFreshJwt(refreshTokenRequest.getUserEmail())
        : ResponseEntity.badRequest().body(new RefreshJwtResponse("Your refresh token is expired",
                "Redirect logout!"));
    }

    //4 queries
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
