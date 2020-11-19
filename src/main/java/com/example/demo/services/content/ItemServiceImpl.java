package com.example.demo.services.content;

import com.example.demo.dto.request.PostItemRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.models.User;
import com.example.demo.models.content.Item;
import com.example.demo.models.content.ItemAnalytic;
import com.example.demo.models.content.SoldItem;
import com.example.demo.models.enums.ItemType;
import com.example.demo.repository.ItemAnalyticRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.SoldItemRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final SoldItemRepository soldItemRepository;
    private final ItemAnalyticRepository itemAnalyticRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, Environment environment, SoldItemRepository soldItemRepository, ItemAnalyticRepository itemAnalyticRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.soldItemRepository = soldItemRepository;
        this.itemAnalyticRepository = itemAnalyticRepository;
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

    public ResponseEntity<?> addItem(String userEmail, Long itemId, boolean addOrRemove) {
        Item item = itemRepository.findByItemId(itemId).orElseThrow(() -> new RuntimeException("Item doesnt exist"));
        User user = userRepository.findByEmailIgnoreCase(userEmail);

        if (user.getCart().containsKey(item)) {
            int coefficient = addOrRemove ? 1 : -1;
            item.setAddedToCart(item.getAddedToCart() + coefficient);
            if (item.getAddedToCart() == 0){
                user.getCart().remove(item);
            }else {
                user.getCart().put(item, user.getCart().get(item) + coefficient);
            }
        } else {
            item.setAddedToCart(1);
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

    public ResponseEntity<?> buyItems(ArrayList<Item> items) {
            items.forEach(item -> {
                Optional<ItemAnalytic> itemAnalyticOptional = itemAnalyticRepository.findBySoldItemId(item.getItemId());
                Optional<SoldItem> soldItemOptional = soldItemRepository.findByItemId(item.getItemId());
                if (itemAnalyticOptional.isPresent()){
                    itemAnalyticOptional.get()
                            .setQuantity(itemAnalyticOptional.get().getQuantity() + item.getAddedToCart());
                    if (soldItemOptional.isPresent()){
                        if (soldItemOptional.get().getItemAnalytic().contains(itemAnalyticOptional.get())){
                            soldItemOptional.get().getItemAnalytic().stream()
                                    .filter(item1 -> item1.getItemName().equals(item.getItemName()))
                                    .forEach(item1 -> item1.setQuantity(item1.getQuantity() + item.getAddedToCart()));
                        }else {
                            soldItemOptional.get().getItemAnalytic().add(itemAnalyticOptional.get());
                        }
                    }else {
                        Set<ItemAnalytic> set = new TreeSet<>();
                        set.add(itemAnalyticOptional.get());
                        soldItemOptional = Optional.ofNullable(SoldItem.builder()
                                .itemAnalytic(set)
                                .itemName(item.getItemName())
                                .itemId(item.getItemId())
                                .build());
                        soldItemRepository.save(soldItemOptional.get());
                    }
                } else {
                     itemAnalyticOptional = Optional.ofNullable(ItemAnalytic.builder()
                            .itemName(item.getItemName())
                            .quantity(item.getAddedToCart())
                            .timeWhenSold(LocalDateTime.now())
                            .soldItemId(item.getItemId())
                            .build());

                    if (soldItemOptional.isPresent()){
                        Set<ItemAnalytic> itemAnalytic1 = soldItemOptional.get().getItemAnalytic();
                        itemAnalytic1.add(itemAnalyticOptional.get());
                        soldItemOptional.get().setItemAnalytic(itemAnalytic1);
                    }
                }
                itemAnalyticRepository.save(itemAnalyticOptional.get());
                soldItemRepository.save(soldItemOptional.get());
            });
        return ResponseEntity.ok(new MessageResponse("Items sold"));

    }

    public Item promoteItem(Item item, Integer newPrice) {
        Item item1 = itemRepository.findByItemId(item.getItemId()).get();
        item1.setDiscount((int) Math.round(100 - (double) newPrice / item1.getPrice() * 100));
        item1.setNewPrice(newPrice);
        itemRepository.save(item1);
        return item1;
    }

    public Item cancelPromoteItem(Item item) {
        Item item1 = itemRepository.findByItemId(item.getItemId()).get();
        item1.setPrice(item1.getPrice());
        item1.setDiscount(0);
        itemRepository.save(item1);
        return item1;
    }

    @Transactional
    public void deleteItem(Item item) {
        itemRepository.deleteByItemId(item.getItemId());
    }

    public Item changeItemQuantity(Item item, Integer quantity) {
        Item item1 = itemRepository.findByItemId(item.getItemId()).get();
        item1.setQuantity(item1.getQuantity() + quantity);
        itemRepository.save(item1);
        return item1;
    }
}
