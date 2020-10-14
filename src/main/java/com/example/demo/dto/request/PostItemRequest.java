package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class PostItemRequest {

    @NotBlank
    @Positive
    private Integer price;

    @Positive
    @NotBlank
    private Integer quantity;

    @NotBlank
    private String itemName;

    @NotBlank
    private String type;

    @NotBlank
    private String itemImageUrl;

    @NotBlank
    private String description;
}
