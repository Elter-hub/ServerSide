package com.example.demo.models;

import com.example.demo.models.enums.EMessage;
import com.example.demo.models.enums.EToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId")
    private long messageId;

    @NotBlank
    private String subject;

    @NotBlank
    private String message;

    @NotBlank
    private String userEmail;

    @NotBlank
    private String recipient;

    private Boolean responded;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private EMessage type;

    public Message(String subject, String message, EMessage type, String email){
        this.message = message;
        this.subject = subject;
        this.type = type;
        this.userEmail = email;
    }

    public Message(String subject, String message, EMessage type, String email, String recipient){
        this.message = message;
        this.subject = subject;
        this.type = type;
        this.userEmail = email;
        this.recipient = recipient;
    }

}
