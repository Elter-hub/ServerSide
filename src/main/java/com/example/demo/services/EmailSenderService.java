package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
public class EmailSenderService {
    @Value("${app.user.email}")
    private String adminEmail;

    SimpleMailMessage mailMessage = new SimpleMailMessage();

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public void sendEmail(String receiver, String subject, String text) {
        mailMessage.setTo(receiver);
        mailMessage.setSubject(subject);
        mailMessage.setFrom(adminEmail);
        mailMessage.setText(text);
        sendEmail(mailMessage);
    }


}
