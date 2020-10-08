package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordRecoverRequest {
    @NotBlank
    private String emailForRecoveringPassword;

    @NotBlank
    private String password;

    @NotBlank
    private String tokenForRecoveringPassword;
}
