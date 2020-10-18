package com.example.demo.services.content;

import com.example.demo.dto.request.PostItemRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.User;
import com.example.demo.models.content.Item;
import com.example.demo.models.enums.ItemType;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@JsonSerialize
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> getItems(ItemType type) {
        return itemRepository.findAllByType(type.name());
    }

    public void postItem(PostItemRequest item) {
        Item newItem = Item.builder().description(item.getDescription())
                .itemImageUrl(item.getItemImageUrl())
                .quantity(item.getQuantity())
                .type(item.getType())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .build();
        itemRepository.save(newItem);
    }

    public ResponseEntity<?> addItem(String userEmail, Long itemId) {
        Item item = itemRepository.findByItemId(itemId).orElseThrow(() -> new RuntimeException("Item doesnt exist"));
        User user = userRepository.findByEmailIgnoreCase(userEmail);

        if (user.getCart().containsKey(item)) {
            user.getCart().put(item, user.getCart().get(item) + 1);
        } else {
            user.getCart().put(item, 1);
        }

        itemRepository.save(item);
        userRepository.save(user);
        return ResponseEntity.ok(new CartResponse(user.getCart().keySet(), user.getCart().values()));
    }

    public ResponseEntity<?> removeItem(String userEmail, Long itemId) {
        Item item = itemRepository.findByItemId(itemId).orElseThrow(() -> new RuntimeException("Item doesnt exist"));
        User user = userRepository.findByEmailIgnoreCase(userEmail);

        user.getCart().remove(item);
        itemRepository.save(item);
        userRepository.save(user);
        return ResponseEntity.ok(new CartResponse(user.getCart().keySet(), user.getCart().values()));
    }

    public ResponseEntity<?> buyItems(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail);
        user.getCart().clear();
        userRepository.save(user);
        return ResponseEntity.ok(new CartResponse(user.getCart().keySet(), user.getCart().values()));
    }
}
