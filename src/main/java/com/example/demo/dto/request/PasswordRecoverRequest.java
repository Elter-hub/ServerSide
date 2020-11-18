package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordRecoverRequest {
    private String emailForRecoveringPassword;

    private String password;

    private String tokenForRecoveringPassword;
}
