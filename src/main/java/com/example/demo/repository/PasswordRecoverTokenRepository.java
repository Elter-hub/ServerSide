package com.example.demo.repository;

import com.example.demo.models.PasswordRecoverToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRecoverTokenRepository extends CrudRepository<PasswordRecoverToken, String> {
    PasswordRecoverToken findByPasswordRecoverToken(String passwordRecoverToken);
    void deleteByUserEmailForPasswordRecovering(String email);
    Optional<PasswordRecoverToken> findByUserEmailForPasswordRecovering(String email);
}
