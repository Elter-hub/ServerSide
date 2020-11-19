package com.example.demo.models.content;

import com.example.demo.models.User;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class SoldItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    private String itemName;

    @OneToMany(mappedBy = "soldItem")
    private Set<ItemAnalytic> itemAnalytic;

    public SoldItem() {

    }
}
