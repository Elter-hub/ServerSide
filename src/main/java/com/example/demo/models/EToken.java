package com.example.demo.models;

public enum EToken {
    JWT_TOKEN("JsonWebToken"),
    E("EmailConfirmationToken"),
    P("TokenForRecoveringPassword");

    EToken(String tokenMessage) {
        this.tokenMessage = tokenMessage;
    }

    String tokenMessage;
    
    public String getToken(EToken token) {
        return tokenMessage;
    }
}
