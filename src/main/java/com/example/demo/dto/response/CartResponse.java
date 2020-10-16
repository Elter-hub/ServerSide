package com.example.demo.dto.response;

import com.example.demo.models.content.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
    private Set<Item> items;
    private Collection<Integer> quantities;
//    private Map<Item, Integer> cart;
}
