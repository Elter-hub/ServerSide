package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class EmailConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long emailTokenId;

    @NotBlank
    private String emailConfirmationToken;

    @CreatedDate
    private LocalDateTime emailConfirmationTokenCreatedDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private User user;

    public EmailConfirmationToken(User user) {
        this.user = user;
        emailConfirmationTokenCreatedDate = LocalDateTime.now();
        emailConfirmationToken = UUID.randomUUID().toString();
    }

    public EmailConfirmationToken() {}
}
