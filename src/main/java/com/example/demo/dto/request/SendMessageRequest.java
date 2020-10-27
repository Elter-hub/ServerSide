package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SendMessageRequest {

    @NotBlank
    private String userEmail;

    @NotBlank
    private String subject;

    @NotBlank
    private String message;
}
