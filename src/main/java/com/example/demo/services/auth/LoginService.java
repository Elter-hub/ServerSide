package com.example.demo.services.auth;

import com.example.demo.dto.response.CartResponse;
import com.example.demo.dto.response.JwtResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public LoginService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                        PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseEntity<JwtResponse> authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        String refreshJwt = UUID.randomUUID().toString();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String encodedRefreshJwt = passwordEncoder.encode(refreshJwt);
        System.out.println("🧰" + refreshJwt);
        System.out.println("🧰" + encodedRefreshJwt);
        userRepository.findByEmailIgnoreCase(email).setRefreshJwtToken(encodedRefreshJwt);
        userRepository.save(userRepository.findByEmailIgnoreCase(email));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshJwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.getAge(),
                userDetails.getUserLastName(),
                userDetails.getUserNickName(),
                userDetails.getImageUrl(),
                new CartResponse(userDetails.getCart().keySet(), userDetails.getCart().values())));
    }
}
