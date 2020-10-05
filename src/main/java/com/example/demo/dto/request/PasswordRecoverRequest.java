package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Not;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordRecoverRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String tokenForRecoveringPassword;
}
