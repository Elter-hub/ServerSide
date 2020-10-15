package com.example.demo.controllers;

import com.example.demo.dto.request.AddItemRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.content.Item;
import com.example.demo.services.content.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("cart")
public class AddToCartController {
    private final ItemService itemService;

    public AddToCartController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PatchMapping("/add-item")
    public ResponseEntity<MessageResponse> addItem(@RequestBody AddItemRequest addItemRequest){
        return itemService.addItem(addItemRequest.getUserEmail(), addItemRequest.getItemId());
    }
}
