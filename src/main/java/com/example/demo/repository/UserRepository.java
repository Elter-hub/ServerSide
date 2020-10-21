package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailIgnoreCase(String emailId);
    Optional<User> findByRefreshJwtToken(String refreshToken);
    Boolean existsByUserNickName(String username);
    Boolean existsByEmail(String email);
}
