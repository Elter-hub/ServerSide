package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import javax.persistence.Column;
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
    @Column(unique = true)
    private String userNickName;

    @NotBlank
    @Size(max = 50)
//    @Email  Shit like me@mail not allowed
    @Pattern(regexp = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")
    @Column(unique = true)
    private String email;

    @JsonIgnore // not sure
    private Set<String> role;

    @NotBlank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])")  //// not sure if correct
    private String password;

    @NotNull
    @Min(12)  // somehow combine
    @Max(80)
    private int age;

    @NotBlank
    @Size(max = 20)
    private String sex;
}
