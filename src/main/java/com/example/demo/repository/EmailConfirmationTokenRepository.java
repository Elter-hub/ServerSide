package com.example.demo.repository;

import com.example.demo.models.EmailConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface EmailConfirmationTokenRepository extends CrudRepository<EmailConfirmationToken, String > {
    EmailConfirmationToken findByEmailConfirmationToken(String emailConfirmationToken);
}
