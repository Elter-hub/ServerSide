package com.example.demo.models;

public enum EToken {
    JWT_TOKEN("JsonWebToken"),
    EMAIL_CONFIRM_TOKEN("EmailConfirmationToken"),
    PASSWORD_RECOVER_TOKEN("TokenForRecoveringPassword");

    EToken(String tokenMessage) {
        this.tokenMessage = tokenMessage;
    }

    String tokenMessage;
    
    public String getToken(EToken token) {
        return tokenMessage;
    }
}
