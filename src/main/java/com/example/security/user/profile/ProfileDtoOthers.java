package com.example.security.user.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProfileDtoOthers {

    private Long id;
    private String firstname;
    private String lastname;
    private String imageUrl;
    private Long phone;
    private String address;
    private String bio;
    private String birthdate;
    private int numberOfFollowers;
    private int numberOfFollowing;
    private boolean isFollowed;
}
