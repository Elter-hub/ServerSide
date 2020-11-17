package com.example.demo.services.auth;

import com.example.demo.dto.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface LoginService {
     ResponseEntity<JwtResponse> authenticateUser(String email, String password);
}
