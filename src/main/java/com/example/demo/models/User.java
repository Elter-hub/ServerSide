package com.example.demo.models;

import com.example.demo.models.content.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])")  //// not sure if correct
    private String password;

    @NotNull
    @Min(12)  // somehow combine
    @Max(80)
    private Integer age;

    @NotBlank
    @Size(max = 20)
    private String sex;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private LocalDateTime createdDate;

    private boolean isEnabled;

    @NotBlank
    @Pattern(regexp = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/*%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    private String imageUrl;

    //Store new password separately, if user confirm by email this password becomes primary
    @NotBlank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])")
    private String temporalPassword;

    @OneToOne(targetEntity = Cart.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private Cart cart;

    @NotNull
    private Long cartId = id;
}
