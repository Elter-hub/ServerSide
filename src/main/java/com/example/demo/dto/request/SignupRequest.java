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
    private String userNickName;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")
    private String email;

    @JsonIgnore
    private Set<String> role;

    @NotBlank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])")
    private String password;

    @NotNull
    @Min(12)
    @Max(80)
    private int age;

    @NotBlank
    @Size(max = 20)
    private String sex;
}
