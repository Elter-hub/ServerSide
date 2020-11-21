package com.example.demo.services.analytic;

import com.example.demo.dto.response.AnalyticResponse;
import com.example.demo.models.content.ItemAnalytic;
import com.example.demo.models.content.SoldItem;
import com.example.demo.repository.ItemAnalyticRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.SoldItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Service
public class AnalyticService {

    private final SoldItemRepository soldItemRepository;
    private final ItemRepository itemRepository;
    private final ItemAnalyticRepository itemAnalyticRepository;

    public AnalyticService(SoldItemRepository soldItemRepository, ItemRepository itemRepository, ItemAnalyticRepository itemAnalyticRepository) {
        this.soldItemRepository = soldItemRepository;
        this.itemRepository = itemRepository;
        this.itemAnalyticRepository = itemAnalyticRepository;
    }

    public ResponseEntity<AnalyticResponse> getTotalSells() {
        List<SoldItem> allSoldItems = soldItemRepository.findAll();
        TreeMap<String , Integer> soldItemsMap = new TreeMap<>();

        allSoldItems.forEach(item -> {
            Long itemId = item.getItemId();
            Integer price = itemRepository.findByItemId(itemId).get().getPrice();
            soldItemsMap.put(item.getItemName(), item.getTotal() * price);
        });
        return ResponseEntity.ok(AnalyticResponse.builder().sum(soldItemsMap).build());
    }

    public ResponseEntity<AnalyticResponse> getEachItemSells() {
        List<SoldItem> allSoldItems = soldItemRepository.findAll();
        TreeMap<String, TreeMap<LocalDateTime, Integer>> finalMap = new TreeMap<>();
        allSoldItems.forEach(item -> {
            TreeMap<LocalDateTime, Integer> soldItemMap = new TreeMap<>();
            Long itemId = item.getItemId();
            Optional<List<ItemAnalytic>> optionalItemAnalyticList = itemAnalyticRepository.findBySoldItemId(itemId);
            optionalItemAnalyticList.ifPresent(itemAnalytics -> itemAnalytics.forEach(itemAnalytic -> {
                soldItemMap.put(itemAnalytic.getTimeWhenSold(), itemAnalytic.getQuantity());
            }));
            finalMap.put(item.getItemName(), soldItemMap);
        });
    return ResponseEntity.ok(AnalyticResponse.builder().singleItem(finalMap).build());
    }
}
