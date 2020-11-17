package com.example.demo.controllers;

import com.example.demo.dto.request.AddItemRequest;
import com.example.demo.services.content.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
public class CartController {
    private final ItemService itemServiceImpl;

    public CartController(ItemService itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @PatchMapping("/add-item")
    public ResponseEntity<?> addItem(@RequestBody AddItemRequest addItemRequest){
        return itemServiceImpl.addItem(addItemRequest.getUserEmail(), addItemRequest.getItemId(), addItemRequest.isAddOrRemove());
    }

    @PatchMapping("/remove-item")
    public ResponseEntity<?> removeItem(@RequestBody AddItemRequest addItemRequest){
        return itemServiceImpl.removeItem(addItemRequest.getUserEmail(), addItemRequest.getItemId());
    }
}
