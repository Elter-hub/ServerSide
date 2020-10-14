package com.example.demo.repository;

import com.example.demo.models.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, String > {
    EmailConfirmationToken findByEmailConfirmationToken(String emailConfirmationToken);
}
