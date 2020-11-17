package com.example.demo.services.auth;

import com.example.demo.dto.response.RefreshJwtResponse;
import org.springframework.http.ResponseEntity;

public interface RefreshTokenService {
    ResponseEntity<RefreshJwtResponse> generateFreshJwt(String email);
    boolean validateRefreshToken(String refreshToken, String email);
}
