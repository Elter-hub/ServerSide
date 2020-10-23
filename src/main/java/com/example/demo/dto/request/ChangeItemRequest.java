package com.example.demo.dto.request;

import com.example.demo.models.content.Item;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
public class ChangeItemRequest {

    @NotBlank
    private Item item;

    @Positive
    private Integer newPrice;

    @Positive
    private Integer newQuantity;
}
