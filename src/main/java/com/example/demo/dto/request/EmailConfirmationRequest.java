package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class EmailConfirmationRequest {
    @NotBlank
    private String requestParam;
}
