package com.example.demo.controllers;

import com.example.demo.dto.request.PostItemRequest;
import com.example.demo.models.content.Item;
import com.example.demo.models.enums.ItemType;
import com.example.demo.services.content.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class ContentController {

    private final ItemService itemService;

    public ContentController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/whiskeys")
    private List<Item> getWhiskeys() {
        return itemService.getItems(ItemType.WHISKEY);
    }

    @PostMapping("/add-item")
//    @PreAuthorize("hasRole('ADMIN')") not working but
//                        .antMatchers("/content/add-item").hasRole("ADMIN") working....

    private void postWhiskey(@RequestBody PostItemRequest request) {
        System.out.println(itemService); // = null WTF???
       itemService.postItem(request);
    }

    @GetMapping("/liquors")
    private List<Item> getLiquors() {
        return itemService.getItems(ItemType.LIQUOR);
    }

    @GetMapping("/roms")
    private List<Item> getRoms() {
        return itemService.getItems(ItemType.ROM);
    }

    @GetMapping("/brandies")
    private List<Item> getBrandies() {
        return itemService.getItems(ItemType.BRANDY);
    }

    @GetMapping("/absents")
    private List<Item> getAbsents() {
        return itemService.getItems(ItemType.ABSENT);
    }

    @GetMapping("/vodkas")
    private List<Item> getVodkas() {
        return itemService.getItems(ItemType.VODKA);
    }
}
