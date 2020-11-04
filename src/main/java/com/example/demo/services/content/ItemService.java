package com.example.demo.services.content;

import com.example.demo.dto.request.ChangeItemRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public ResponseEntity<?> buyItems(String userEmail, ArrayList<Item> items,
                                      ArrayList<Integer> quantities) {
        User user = userRepository.findByEmailIgnoreCase(userEmail);
        for (int i = 0; i < items.size(); i++){
            Item item = itemRepository.findByItemId(items.get(i).getItemId()).get();
            item.setQuantity(item.getQuantity() - quantities.get(i));
            user.getCart().put(item, quantities.get(i));
            itemRepository.save(item);
        }
        userRepository.save(user);
        return ResponseEntity.ok(new CartResponse(user.getCart().keySet(), user.getCart().values()));
    }

    public Item promoteItem(Item item, Integer newPrice){
        Item item1 = itemRepository.findByItemId(item.getItemId()).get();
        item1.setDiscount((int) Math.round(100 - (double) newPrice/item1.getPrice()*100));
        item1.setNewPrice(newPrice);
        itemRepository.save(item1);
        return item1;
    }

    public Item cancelPromoteItem(Item item){
        Item item1 = itemRepository.findByItemId(item.getItemId()).get();
        item1.setPrice(item1.getPrice());
        item1.setDiscount(0);
        itemRepository.save(item1);
        return item1;
    }

    @Transactional
    public void deleteItem(Item item){
        itemRepository.deleteByItemId(item.getItemId());
    }

    public Item changeItemQuantity(Item item, Integer quantity){
        Item item1 = itemRepository.findByItemId(item.getItemId()).get();
        item1.setQuantity(item1.getQuantity() + quantity);
        itemRepository.save(item1);
        return item1;
    }
}
