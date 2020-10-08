package com.example.demo.controllers;

import com.example.demo.dto.request.PasswordRecoverRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CurrentUser;
import com.example.demo.services.UserChangePasswordService;
import com.example.demo.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private final UserChangePasswordService userChangePasswordService;

    public UserController(UserChangePasswordService userChangePasswordService) {
        this.userChangePasswordService = userChangePasswordService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserDetailsImpl userDetails) {
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> checkUserOldPassword(@RequestBody PasswordRecoverRequest userChangePasswordRequest){

        userChangePasswordService.checkOldPasswordValidity(userChangePasswordRequest.getEmailForRecoveringPassword(), userChangePasswordRequest.getPassword());
//        userChangePasswordService.setNewPasswordAfterEmailConfirmation()
        return ResponseEntity.ok(new MessageResponse("Please Check your email to confirm changing password"));
    }
}
