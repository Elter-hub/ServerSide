package com.example.demo.dto.response;

import com.example.demo.models.content.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String userName;
    private String userLastName;
    private String userNickName;
    private Integer userAge;
    private String userEmail;
    private String imageUrl;
    private List<String> roles;
    private List<Item> cart;

    public JwtResponse(String accessToken, Long id, String userName, String userEmail,
                       List<String> roles, Integer userAge, String userLastName, String userNickName,
                       String imageUrl, List<Item> cart) {
        this.token = accessToken;
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.roles = roles;
        this.userAge = userAge;
        this.userLastName = userLastName;
        this.userNickName = userNickName;
        this.imageUrl = imageUrl;
        this.cart = cart;
    }
}
