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
    private String userName;
    private String userLastName;
    private Integer userAge;
    private String userEmail;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String userName, String userEmail,
                       List<String> roles, Integer userAge, String userLastName ) {
        this.token = accessToken;
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.roles = roles;
        this.userAge = userAge;
        this.userLastName = userLastName;
    }
}
