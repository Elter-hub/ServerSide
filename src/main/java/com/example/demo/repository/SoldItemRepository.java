package com.example.demo.repository;

import com.example.demo.models.content.SoldItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SoldItemRepository extends JpaRepository<SoldItem, String> {
    SoldItem findById(Long soldItemId);
    List<SoldItem> findAll();
    Optional<SoldItem> findByItemId(Long itemId);
}
