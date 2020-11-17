package com.example.demo.models;

import com.example.demo.models.enums.EToken;
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
    @Column(name = "token")
    private String emailConfirmationToken;

    @NotBlank
    @Column(name = "userEmail")
    private String emailConfirmationTokenUserEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EToken tokenType = EToken.E;

    @CreatedDate
    @Column(name = "date")
    private LocalDateTime emailConfirmationTokenCreatedDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "id")
    private User user;

    public EmailConfirmationToken(User user) {
        this.user = user;
        emailConfirmationTokenUserEmail = user.getEmail();
        emailConfirmationTokenCreatedDate = LocalDateTime.now();
        emailConfirmationToken = UUID.randomUUID().toString();
    }

    public EmailConfirmationToken() {}
}
