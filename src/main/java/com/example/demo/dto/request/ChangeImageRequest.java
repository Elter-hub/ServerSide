package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangeImageRequest {
    @NotBlank
    private String imageUrl;

    @NotBlank
    private String userEmail;
}
