package com.example.demo.services.analytic;

import com.example.demo.dto.response.TotalSellingResponse;
import com.example.demo.models.content.SoldItem;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.SoldItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;

@Service
public class AnalyticService {

    private final SoldItemRepository soldItemRepository;
    private final ItemRepository itemRepository;

    public AnalyticService(SoldItemRepository soldItemRepository, ItemRepository itemRepository) {
        this.soldItemRepository = soldItemRepository;
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<TotalSellingResponse> getTotalSells() {
        List<SoldItem> allSoldItems = soldItemRepository.findAll();
        TreeMap<String , Integer> soldItemsMap = new TreeMap<>();

        allSoldItems.stream().forEach(item -> {
            Long itemId = item.getItemId();
            Integer price = itemRepository.findByItemId(itemId).get().getPrice();
            soldItemsMap.put(item.getItemName(), item.getTotal() * price);
        });
        return ResponseEntity.ok(new TotalSellingResponse(soldItemsMap));
    }
}
