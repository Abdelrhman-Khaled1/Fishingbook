package com.example.security.user.follow;

import com.example.security.user.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow/{followedId}")
    public ResponseEntity<Void> followUser(@PathVariable Long followedId) {
        followService.followUser(followedId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unfollow/{followedId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followedId) {
        followService.unfollowUser(followedId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/getFollowers/{id}")
    public List<UserSummary> getFollowers(@PathVariable Long id){
        return followService.getFollowers(id);
    }

    @GetMapping("/getFollowing/{id}")
    public List<UserSummary> getFollowedList(@PathVariable Long id){
        return followService.getFollowed(id);
    }
}
