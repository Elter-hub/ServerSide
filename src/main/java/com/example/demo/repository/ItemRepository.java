package com.example.demo.repository;

import com.example.demo.models.content.Item;
import com.example.demo.models.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    List<Item> findAll();
    Optional<Item> findByItemId(Long itemId);
    List<Item> findAllByType(@NotNull String type);
    void deleteByItemId(Long id);
}
