package com.example.demo.services.content;

import com.example.demo.dto.request.PostItemRequest;
import com.example.demo.models.content.Item;
import com.example.demo.models.enums.ItemType;
import com.example.demo.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getItems(ItemType type){
        return itemRepository.findAllByType(type.name());
    }

    public void postItem(PostItemRequest item){
        Item newItem = Item.builder().description(item.getDescription())
                .itemImageUrl(item.getItemImageUrl())
                .quantity(item.getQuantity())
                .type(item.getType())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .build();
        itemRepository.save(newItem);
    }






}
