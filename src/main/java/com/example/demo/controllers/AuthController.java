package com.example.demo.controllers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.services.EmailSenderService;
import com.example.demo.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    private final TokenActionsRepository tokenActionsRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder encoder, JwtTokenProvider jwtTokenProvider, EmailConfirmationTokenRepository emailConfirmationTokenRepository, EmailSenderService emailSenderService, TokenActionsRepository tokenActionsRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.emailSenderService = emailSenderService;
        this.tokenActionsRepository = tokenActionsRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.getAge(),
                userDetails.getUserLastName(),
                userDetails.getUserNickName(),
                userDetails.getImageUrl()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserNickName(signUpRequest.getUserNickName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User Nick name is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = User.builder().userName(signUpRequest.getUserName())
                .userLastName(signUpRequest.getUserLastName())
                .userNickName(signUpRequest.getUserNickName())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .age(signUpRequest.getAge())
                .sex(signUpRequest.getSex())
                .createdDate(LocalDateTime.now())
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(user);
        emailConfirmationTokenRepository.save(emailConfirmationToken);
        TokenActions tokenActions = tokenActionsRepository.findByUserId(user.getId()).orElse(
                tokenActionsRepository.save(new TokenActions(emailConfirmationToken, user))
        );
        //if token isn't present the next line is useless, but it's needed in other case
        tokenActions.setEmailConfirmationToken(emailConfirmationToken.getEmailConfirmationToken());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("ihor04@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:4200/confirm?token=" + emailConfirmationToken.getEmailConfirmationToken());

        emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmUserAccount(@RequestBody EmailConfirmationRequest requestEmailToken) {
        System.out.println(requestEmailToken.getEmailConfirmationToken());
        EmailConfirmationToken token = emailConfirmationTokenRepository.findByEmailConfirmationToken(requestEmailToken.getEmailConfirmationToken());
        if (token != null) {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Email ok"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Email problem"));
    }
}
