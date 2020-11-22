package com.example.demo.models;

import com.example.demo.models.content.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+")
    @Size(min = 3, max = 20)
    private String userName;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+")
    @Size(min = 3, max = 20)
    private String userLastName;

    @NotBlank
    private String userNickName;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")
    private String email;

    @NotBlank
    private String password;

    @NotNull
    @Min(12)
    @Max(80)
    private Integer age;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private LocalDateTime createdDate;

    private boolean isEnabled;

    @NotBlank
    @Pattern(regexp = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/*%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    private String imageUrl;

    private String temporalPassword;

    @ElementCollection
    @CollectionTable(name = "user_items",
            joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "item_id")
    @Column(name = "quantity")
    private Map<Item, Integer> cart = new HashMap<>();

    private String refreshJwtToken;

    private LocalDateTime refreshExpiration;
}
