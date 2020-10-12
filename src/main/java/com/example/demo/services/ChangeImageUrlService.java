package com.example.demo.services;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChangeImageUrlService {

    private final UserRepository userRepository;

    public ChangeImageUrlService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<MessageResponse> changeImageUrl(String imageUrl, String userEmail) {
        User user = this.userRepository.findByEmailIgnoreCase(userEmail);
        user.setImageUrl(imageUrl);
        this.userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Image was successfully changed!"));
    }
}
