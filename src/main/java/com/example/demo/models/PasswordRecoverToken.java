package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
public class PasswordRecoverToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenId")
    private long passwordRecoverTokenId;

    @Column(name = "token")
    private String passwordRecoverToken;
    @Column(name = "userEmail")
    private String  userEmailForPasswordRecovering;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EToken tokenType = EToken.P;

    @CreatedDate
    @Column(name = "Date")
    private LocalDateTime passwordConfirmationTokenCreatedDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private User user;

    public PasswordRecoverToken(User user) {
        this.user = user;
        userEmailForPasswordRecovering = user.getEmail();
        passwordConfirmationTokenCreatedDate = LocalDateTime.now();
        passwordRecoverToken = generateToken();
    }

    public PasswordRecoverToken() {}

    private String generateToken() {
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }
}
