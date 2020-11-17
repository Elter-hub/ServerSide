package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface ChangeImageUrlService {
    ResponseEntity<MessageResponse> changeImageUrl(String imageUrl, String userEmail);

    }
