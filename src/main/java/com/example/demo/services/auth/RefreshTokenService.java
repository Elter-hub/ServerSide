package com.example.demo.services.auth;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    private final UserRepository userRepository;

    public RefreshTokenService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    public boolean validateRefreshToken(String refreshToken, String email){
        User user = userRepository.findByEmailIgnoreCase(email);
        System.out.println(user);
        System.out.println(refreshToken);
        System.out.println("SUPERUSER " + BCrypt.checkpw("Superuser123", user.getPassword()));
        System.out.println(BCrypt.checkpw(refreshToken, user.getRefreshJwtToken()));

        if (BCrypt.checkpw(refreshToken, user.getRefreshJwtToken())){
            System.out.println(" 🦮🦮🦮🦮🦮🦮🦮  Refresh token matches");
            return true;
        }
        return false;
    }
}
