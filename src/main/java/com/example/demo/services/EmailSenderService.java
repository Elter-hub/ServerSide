package com.example.demo.services;

import org.springframework.mail.SimpleMailMessage;

public interface EmailSenderService {
    void sendEmail(String receiver, String subject, String text);
    void sendEmail(SimpleMailMessage email);
}
