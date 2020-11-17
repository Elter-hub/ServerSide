package com.example.demo.services.implementation;

import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.ChangeImageUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
class ChangeImageUrlServiceImpl implements ChangeImageUrlService {

    private final UserRepository userRepository;

    public ChangeImageUrlServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<MessageResponse> changeImageUrl(String imageUrl, String userEmail) {
        User user = this.userRepository.findByEmailIgnoreCase(userEmail);
        user.setImageUrl(imageUrl);
        this.userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Image was successfully changed!"));
    }
}
