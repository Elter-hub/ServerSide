package com.example.demo.services.auth;

import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.*;
import com.example.demo.models.enums.ERole;
import com.example.demo.repository.EmailConfirmationTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TokenActionsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.EmailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final TokenActionsRepository tokenActionsRepository;
    private final EmailSenderService emailSenderService;

    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                           EmailConfirmationTokenRepository emailConfirmationTokenRepository,
                           TokenActionsRepository tokenActionsRepository, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.tokenActionsRepository = tokenActionsRepository;
        this.emailSenderService = emailSenderService;
    }


    public ResponseEntity<MessageResponse> registerUser(SignupRequest request) {
        // checking for uniqueness
        // should be replaced with annotation for conciseness
        // TODO create custom validator for field uniqueness
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

        //Cant create without builder cause id is autoincrement
        // Additional constructor excluding field Id takes lot of space
        User user = User.builder()
                .userName(request.getUserName())
                .userLastName(request.getUserLastName())
                .userNickName(request.getUserNickName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .sex(request.getSex())
                //default image, then on client side user can change
                .imageUrl("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.e1KNYwnuhNwNj7_-98yTRwHaF7%26pid%3DApi&f=1")
                .createdDate(LocalDateTime.now())
                .build();

        //By default all users get ROLE_USER
        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        //Dont quite understand next lines
        //Always true ⬇⬇⬇⬇⬇ when registering from Client side
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
        //Additional entity consisting of EmailConfirmation and PasswordRecover tokens
        tokenActionsRepository.save(new TokenActions(emailConfirmationToken, user));

        emailSenderService.sendEmail(user.getEmail(), "Complete Registration!", "To confirm your account, please click here : "
                + "http://localhost:4200/confirm?token=" + emailConfirmationToken.getEmailConfirmationToken());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
