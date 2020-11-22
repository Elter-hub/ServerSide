package com.example.demo.services.content;

import com.example.demo.dto.request.PostItemRequest;
import com.example.demo.models.content.Item;
import com.example.demo.models.enums.ItemType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface ItemService {
     List<Item> getAllItems();
     List<Item> getItems(ItemType type);
     void postItem(PostItemRequest item);
     ResponseEntity<?> addItem(String userEmail, Long itemId, boolean addOrRemove);
     ResponseEntity<?> removeItem(String userEmail, Long itemId);
     ResponseEntity<?> buyItems(ArrayList<Item> items, String userEmail);
     Item promoteItem(Item item, Integer newPrice);
     Item cancelPromoteItem(Item item);
     void deleteItem(String  itemId);
     Item changeItemQuantity(Item item, Integer quantity);









    }
