package com.example.demo.services.auth;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RefreshTokenService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateRefreshToken(String refreshToken, String email){
        System.out.println("🧮🧮🧮🧮🧮🧮🧮🧮");
        System.out.println("REFRESH TOKEN" + refreshToken);
        System.out.println("🧮🧮🧮🧮🧮🧮🧮🧮");
        User user = userRepository.findByEmailIgnoreCase(email);

        System.out.println("🧱🧱🧱🧱🧱🧱🧱🧱🧱");
        System.out.println("USER ENCODED REFRESH TOKEN" + user.getRefreshJwtToken());
        System.out.println("🧱🧱🧱🧱🧱🧱🧱🧱🧱");
//        if (!BCrypt.checkpw(userOldPassword, user.getPassword())) {

        System.out.println("BCRYPT" + BCrypt.checkpw(refreshToken, user.getRefreshJwtToken()));
        System.out.println("EQUALS" + refreshToken.equals(user.getRefreshJwtToken()));
        System.out.println("PASSWORD ENCODER" + passwordEncoder.matches(refreshToken, user.getRefreshJwtToken()));
        if (BCrypt.checkpw(refreshToken, user.getRefreshJwtToken())){
            System.out.println("🦮🦮🦮🦮🦮🦮🦮");
            System.out.println("Refresh token matches");
            System.out.println("🦮🦮🦮🦮🦮🦮🦮");
            return true;
        }
        return false;
    }
}
