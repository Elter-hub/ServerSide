package com.example.demo.dto.response;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
public class RefreshJwtResponse {

    @NotBlank
    String accessToken;

    @NotBlank
    String refreshToken;
}
