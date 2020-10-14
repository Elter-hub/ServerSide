package com.example.demo.repository;

import com.example.demo.models.TokenActions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenActionsRepository extends JpaRepository<TokenActions, String> {
    Optional<TokenActions> findByUserId(Long userId);
}
