package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshTokenRequest {

    @NotBlank
    private String userEmail;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
