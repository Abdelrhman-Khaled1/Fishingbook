package com.example.security.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class UserDtoResponse {

    private Long id;
    @NotBlank
    private String firstname;
    private String lastname;
    @URL
    private String imageUrl;
    private Long phone;
    private String address;
    private String bio;
}


