package com.example.demo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
public class PasswordRecoverToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long passwordRecoverTokenId;

    private String passwordRecoverToken;
    private String  userEmailForPasswordRecovering;

    @CreatedDate
    private LocalDateTime passwordConfirmationTokenCreatedDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private User user;

    public PasswordRecoverToken(User user) {
        this.user = user;
        passwordConfirmationTokenCreatedDate = LocalDateTime.now();
        passwordRecoverToken = UUID.randomUUID().toString();
    }

    public PasswordRecoverToken() {}
}
