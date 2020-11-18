package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private String userName;
    private String userLastName;
    private String userNickName;
    private Integer userAge;
    private String userEmail;
    private String imageUrl;
    private List<String> roles;
    private CartResponse cart;
    private Boolean isEnabled;

    public JwtResponse(String accessToken, String refreshToken, Long id, String userName, String userEmail,
                       List<String> roles, Integer userAge, String userLastName, String userNickName,
                       String imageUrl, CartResponse cart, Boolean isEnabled) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.roles = roles;
        this.userAge = userAge;
        this.userLastName = userLastName;
        this.userNickName = userNickName;
        this.imageUrl = imageUrl;
        this.cart = cart;
        this.isEnabled = isEnabled;
    }
}
