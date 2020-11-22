package com.example.demo.models.content;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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

    @Positive
    private Integer newPrice;

    @PositiveOrZero
    private Integer discount;

    @PositiveOrZero
    private Integer quantity;

    @NotBlank
    private String itemName;

    private Integer addedToCart;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotBlank
    private String itemImageUrl;

    @NotBlank
    private String description;

    private Double stars;
}
