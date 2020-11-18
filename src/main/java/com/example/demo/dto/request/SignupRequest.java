package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Set;
import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+", message = "Only Letters" )
    @Size(min = 3, max = 20)
    private String userName;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+", message = "Only Letters")
    @Size(min = 3, max = 20)
    private String userLastName;

    @NotBlank
    private String userNickName;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b", message = "Should be valid email📧")
    private String email;

    @JsonIgnore
    private Set<String> role;

    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$")
    private String password;

    @NotNull
    @Min(12)
    @Max(80)
    private int age;
}
