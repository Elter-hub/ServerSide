package com.example.demo.models.content;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ItemAnalytic implements Comparable<ItemAnalytic>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timeWhenSold;
    private Integer quantity;
    private Long soldItemId;
    private String itemName;


    @ManyToOne
    @JoinColumn(name = "item_id")
    private SoldItem soldItem;

    public ItemAnalytic() {

    }

    @Override
    public int compareTo(ItemAnalytic time) {
        return this.timeWhenSold.isAfter(time.timeWhenSold) ? 1 : -1;
    }
}
