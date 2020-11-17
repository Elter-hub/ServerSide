package com.example.demo.services.auth.implementation;

import com.example.demo.dto.response.RefreshJwtResponse;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.services.implementation.UserDetailsServiceImpl;
import com.example.demo.services.auth.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${app.refreshJwt.expirationMs}")
    private Long refreshExpiration;

    public RefreshTokenServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean validateRefreshToken(String refreshToken, String email){
        User user = userRepository.findByEmailIgnoreCase(email);
        return BCrypt.checkpw(refreshToken, user.getRefreshJwtToken())
                && user.getRefreshExpiration().isAfter(LocalDateTime.now());
    }

    @Override
    public ResponseEntity<RefreshJwtResponse> generateFreshJwt(String email){
        User user = userRepository.findByEmailIgnoreCase(email);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateJwtToken(authentication);

        String refreshJwt = UUID.randomUUID().toString();
        String encodedRefreshJwt = passwordEncoder.encode(refreshJwt);
        user.setRefreshJwtToken(encodedRefreshJwt);
        user.setRefreshExpiration(LocalDateTime.now().plusSeconds(refreshExpiration));
        userRepository.save(userRepository.findByEmailIgnoreCase(email));

        return ResponseEntity.ok(new RefreshJwtResponse(accessToken, refreshJwt));
    }
}
