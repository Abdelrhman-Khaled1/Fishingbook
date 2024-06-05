package com.example.security.user.profile;

import com.example.security.auth.AuthenticationService;
import com.example.security.user.User;
import com.example.security.user.UserService;
import com.example.security.user.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    private final FollowService followService;

    public ProfileDtoMine getMyProfile() {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        return new ProfileDtoMine(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getImageUrl(),
                user.getPhone(),
                user.getAddress(),
                user.getBio(),
                user.getBirthdate() != null ? user.getBirthdate().toString() : null,
                user.getNumberOfFollowers(),
                user.getNumberOfFollowing()
        );
    }

    public ProfileDtoOthers getOthersProfileById(Long id) {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        User userProfile = userService.findById(id);

        boolean followed = followService.isFollowed(user, userProfile);

        return new ProfileDtoOthers(
                userProfile.getId(),
                userProfile.getFirstname(),
                userProfile.getLastname(),
                userProfile.getImageUrl(),
                userProfile.getPhone(),
                userProfile.getAddress(),
                userProfile.getBio(),
                userProfile.getBirthdate() != null ? userProfile.getBirthdate().toString() : null,
                userProfile.getNumberOfFollowers(),
                userProfile.getNumberOfFollowing(),
                followed
        );
    }
}
