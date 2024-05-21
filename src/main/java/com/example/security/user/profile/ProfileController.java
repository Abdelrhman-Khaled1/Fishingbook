package com.example.security.user.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ProfileDtoMine getMyProfile(){
        return profileService.getMyProfile();
    }

    @GetMapping("/{userId}")
    public ProfileDtoOthers getUserProfileById(@PathVariable Long userId){
        return profileService.getOthersProfileById(userId);
    }


}
