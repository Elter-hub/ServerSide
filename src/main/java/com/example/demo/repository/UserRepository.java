package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    User findByEmailIgnoreCase(String emailId);
    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);
}
