package com.example.demo.dto.request;

import com.example.demo.models.content.Item;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Getter
public class BuyItemsRequest {

    @NotBlank
    private ArrayList<Item> items;

    @NotBlank
    private ArrayList<Integer> quantities;
}
