package com.example.demo.controllers;

import com.example.demo.dto.request.ChangeImageRequest;
import com.example.demo.dto.request.PasswordRecoverRequest;
import com.example.demo.dto.request.UserChangePasswordRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class UserController {

    private final UserChangePasswordService userChangePasswordService;
    private final UserRecoverPasswordService userRecoverPasswordService;
    private final ChangeImageUrlService changeImageUrlService;
    private final EmailSenderService emailSenderService;

    public UserController(UserChangePasswordService userChangePasswordService, UserRecoverPasswordService userRecoverPasswordService,
                          ChangeImageUrlService changeImageUrlService, EmailSenderService emailSenderService) {
        this.userChangePasswordService = userChangePasswordService;
        this.userRecoverPasswordService = userRecoverPasswordService;
        this.changeImageUrlService = changeImageUrlService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> checkUserOldPassword(@RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        ResponseEntity<MessageResponse> checkAndForgotPassword = userChangePasswordService.checkOldPasswordValidity(userChangePasswordRequest.getUserEmail(),
                userChangePasswordRequest.getOldPassword());
        return checkAndForgotPassword.getStatusCodeValue() == 400
                ? checkAndForgotPassword
                : userRecoverPasswordService.userResetPassword(userChangePasswordRequest.getUserEmail(),
                userChangePasswordRequest.getNewPassword());
    }

    @PostMapping("/confirm-password")
    public ResponseEntity<MessageResponse> confirmUserChangePassword(@RequestBody PasswordRecoverRequest passwordRecoverRequest) {
        return userRecoverPasswordService.confirmPasswordChanges(passwordRecoverRequest.getEmailForRecoveringPassword(),
                passwordRecoverRequest.getTokenForRecoveringPassword());
    }

    @PutMapping("change-image")
    public ResponseEntity<MessageResponse> changeUserImageUrl(@RequestBody ChangeImageRequest url) {
        return this.changeImageUrlService.changeImageUrl(url.getImageUrl(), url.getUserEmail());
    }
}
