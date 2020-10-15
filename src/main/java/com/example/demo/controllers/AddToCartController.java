package com.example.demo.controllers;

import com.example.demo.dto.request.AddItemRequest;
import com.example.demo.services.content.ItemService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("cart")
public class AddToCartController {
    private final ItemService itemService;

    public AddToCartController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/add-item")
    public void addItem(@RequestBody AddItemRequest addItemRequest){
        itemService.addItem(addItemRequest.getUserEmail(), addItemRequest.getItemId());
    }
}
