package com.example.demo.repository;

import com.example.demo.models.PasswordRecoverToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoverTokenRepository extends CrudRepository<PasswordRecoverToken, String> {
    PasswordRecoverToken findByPasswordRecoverToken(String passwordRecoverToken);
}
