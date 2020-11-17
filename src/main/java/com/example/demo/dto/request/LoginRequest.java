package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    private String userName;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
