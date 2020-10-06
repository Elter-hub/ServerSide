package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class PasswordRecoverToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long passwordRecoverTokenId;

    private String passwordRecoverToken;

    @CreatedDate
    private LocalDateTime passwordConfirmationTokenCreatedDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private User user;
}
