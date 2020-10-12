package com.example.demo.controllers;

import com.example.demo.dto.request.ChangeImageRequest;
import com.example.demo.dto.request.PasswordRecoverRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.ChangeImageUrlService;
import com.example.demo.services.UserChangePasswordService;
import com.example.demo.services.UserRecoverPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private final UserChangePasswordService userChangePasswordService;
    private final UserRecoverPasswordService userRecoverPasswordService;
    private final ChangeImageUrlService changeImageUrlService;

    public UserController(UserChangePasswordService userChangePasswordService, UserRecoverPasswordService userRecoverPasswordService, ChangeImageUrlService changeImageUrlService) {
        this.userChangePasswordService = userChangePasswordService;
        this.userRecoverPasswordService = userRecoverPasswordService;
        this.changeImageUrlService = changeImageUrlService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> checkUserOldPassword(@RequestBody PasswordRecoverRequest userChangePasswordRequest){
        ResponseEntity<MessageResponse> validity = userChangePasswordService.checkOldPasswordValidity(userChangePasswordRequest.getEmailForRecoveringPassword(),
                userChangePasswordRequest.getPassword());
        return validity.getStatusCodeValue() == 400
                ? ResponseEntity.badRequest().body(new MessageResponse("Old password is incorrect"))
                : userRecoverPasswordService.forgotPassword(userChangePasswordRequest.getEmailForRecoveringPassword());
    }

    @PutMapping("change-image")
    public ResponseEntity<MessageResponse> changeUserImageUrl(@RequestBody ChangeImageRequest url){
       return this.changeImageUrlService.changeImageUrl(url.getImageUrl(), url.getUserEmail());
    }
}
