package com.example.security.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Getter
@Setter
public class UserDtoRequest {

    @NotBlank
    private String firstname;
    private String lastname;
    @URL
    private String imageUrl;
    private Long phone;
    private String address;
    private String bio;
    private LocalDate birthdate;
}


