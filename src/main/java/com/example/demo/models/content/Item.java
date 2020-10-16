package com.example.demo.models.content;

import com.example.demo.models.User;
import com.example.demo.models.enums.EToken;
import com.example.demo.models.enums.ItemType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Positive
    private Integer price;

    @PositiveOrZero
    private Integer quantity;

    @NotBlank
    private String itemName;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotBlank
    private String itemImageUrl;

    @NotBlank
    private String description;

    @NotBlank
    private Double stars;
}
