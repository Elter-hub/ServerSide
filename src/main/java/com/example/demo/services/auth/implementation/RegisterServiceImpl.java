package com.example.demo.services.auth.implementation;

import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.*;
import com.example.demo.models.enums.ERole;
import com.example.demo.repository.EmailConfirmationTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TokenActionsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.EmailSenderService;
import com.example.demo.services.auth.RegisterService;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final TokenActionsRepository tokenActionsRepository;
    private final EmailSenderService emailSenderService;
    private final Environment environment;


    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                               EmailConfirmationTokenRepository emailConfirmationTokenRepository,
                               TokenActionsRepository tokenActionsRepository, EmailSenderService emailSenderServiceImpl, Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.tokenActionsRepository = tokenActionsRepository;
        this.emailSenderService = emailSenderServiceImpl;
        this.environment = environment;
    }

    @Override
    public ResponseEntity<MessageResponse> registerUser(@Valid SignupRequest request) {
        if (userRepository.existsByUserNickName(request.getUserNickName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User Nick name is already taken!"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = User.builder()
                .userName(request.getUserName())
                .userLastName(request.getUserLastName())
                .userNickName(request.getUserNickName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .imageUrl(environment.getProperty("app.default.image"))
                .createdDate(LocalDateTime.now())
                .build();

        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
//            Creating Admin
            Optional<Role> admin = roleRepository.findByName(ERole.ROLE_ADMIN);
            roles.add(admin.get());
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

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
        tokenActionsRepository.save(new TokenActions(emailConfirmationToken, user));

        emailSenderService.sendEmail(user.getEmail(), "Complete Registration!",
                "Enter this token: " +  emailConfirmationToken.getEmailConfirmationToken());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
