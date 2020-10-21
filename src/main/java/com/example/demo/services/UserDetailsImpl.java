package com.example.demo.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.demo.models.User;
import com.example.demo.models.content.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ToString
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private final Long id;
    private final String username;
    private final String email;
    private final String userLastName;
    private final String userNickName;
    private  String refreshJwtToken;
    private final Integer age;
    private final String  imageUrl;
    private final String password;
    private final Map<Item, Integer> cart;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String userLastName, String userNickName, String refreshJwtToken, Integer age,
                           String imageUrl, String password, Map<Item, Integer> cart, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userLastName = userLastName;
        this.userNickName = userNickName;
        this.refreshJwtToken = refreshJwtToken;
        this.age = age;
        this.imageUrl = imageUrl;
        this.password = password;
        this.cart = cart;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getUserLastName(),
                user.getUserNickName(),
                user.getRefreshJwtToken(),
                user.getAge(),
                user.getImageUrl(),
                user.getPassword(),
                user.getCart(),
                authorities);
    }

    public Map<Item, Integer> getCart() {
        return cart;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setRefreshJwtToken(String refreshJwtToken) {
        this.refreshJwtToken = refreshJwtToken;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}

