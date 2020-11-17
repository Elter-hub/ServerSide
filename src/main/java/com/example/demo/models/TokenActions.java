package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Entity
public class TokenActions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String  emailConfirmationToken;

    private String  passwordRecoverToken;

    @NotNull
    private Long userId;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "id")
    private User user;

    public TokenActions(@NotNull EmailConfirmationToken emailConfirmationToken, User user) {
        this.user = user;
        this.userId = user.getId();
        this.emailConfirmationToken = emailConfirmationToken.getEmailConfirmationToken();
    }

    public TokenActions(@NotNull PasswordRecoverToken passwordRecoverToken, User user ) {
        this.user = user;
        this.userId = user.getId();
        this.passwordRecoverToken = passwordRecoverToken.getPasswordRecoverToken();
    }
    public TokenActions() {}
}
