package com.example.demo.services.auth;

import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

public interface RegisterService {
    ResponseEntity<MessageResponse> registerUser(@Valid SignupRequest request);
}
