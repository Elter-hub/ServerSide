package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+")
    @Size(min = 3, max = 20)
    private String userName;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+")
    @Size(min = 3, max = 20)
    private String userLastName;

    @NotBlank
//    @Column(unique = true)
    // when creating a Bean it's check for duplicates
    private String userNickName;

    @NotBlank
    @Size(max = 50)
//    @Email  Shit like me@mail not allowed
    @Pattern(regexp = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")
//    @Column(unique = true)  // when creating an Object it's check for duplicates
    private String email;

    @JsonIgnore // not sure
    private Set<String> role;

    @NotBlank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])")  //// not sure if correct
//    @JsonIgnore
    private String password;

    @NotNull
    @Min(12)  // somehow combine
    @Max(80)
    private int age;

    @NotBlank
    @Size(max = 20)
    private String sex;
}
