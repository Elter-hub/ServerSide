package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String userLastName;
    private Integer userAge;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email,
                       List<String> roles, Integer userAge, String userLastName ) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.userAge = userAge;
        this.userLastName = userLastName;
    }
}
