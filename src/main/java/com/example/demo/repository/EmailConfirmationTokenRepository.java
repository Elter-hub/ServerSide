package com.example.demo.repository;

import com.example.demo.models.EmailConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailConfirmationTokenRepository extends CrudRepository<EmailConfirmationToken, String > {
    EmailConfirmationToken findByEmailConfirmationToken(String emailConfirmationToken);
}
