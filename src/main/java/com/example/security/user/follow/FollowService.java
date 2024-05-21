package com.example.security.user.follow;

import com.example.security.auth.AuthenticationService;
import com.example.security.user.User;
import com.example.security.user.UserService;
import com.example.security.user.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserService userService;

    private final AuthenticationService authenticationService;
    private final FollowRepository followRepository;


    public List<UserSummary> getFollowers(Long userId) {
        User user = userService.findById(userId);
        List<Follow> followers = followRepository.findByFollowed(user);//get people which follow user
        return followers.stream().map(
                follower -> new UserSummary(
                        follower.getFollower().getId(),
                        follower.getFollower().getFirstname(),
                        follower.getFollower().getImageUrl()
                ))
                .sorted(Comparator.comparing(UserSummary::getName))
                .collect(Collectors.toList());
    }

    public List<UserSummary> getFollowed(Long userId) {
        User user = userService.findById(userId);
        List<Follow> followedList = followRepository.findByFollower(user);//get people user follow them
        return followedList.stream().map(
                follower -> new UserSummary(
                        follower.getFollowed().getId(),
                        follower.getFollowed().getFirstname(),
                        follower.getFollowed().getImageUrl()
                ))
                .sorted(Comparator.comparing(UserSummary::getName))
                .collect(Collectors.toList());
    }

    @Transactional
    public void followUser(Long id) {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User follower = userService.findByEmail(loggedInUser.getUsername()).get();

        User followed = userService.findById(id);

        if (followRepository.findByFollowerAndFollowed(follower, followed).isPresent()) {
            throw new IllegalStateException("Already following this user");
        }
        if (follower.equals(followed)) {
            throw new IllegalStateException("Can't follow your self");
        }

        Follow follow = new Follow(follower, followed);
        followRepository.save(follow);

        follower.setNumberOfFollowing(follower.getNumberOfFollowing()+1);
        followed.setNumberOfFollowers(followed.getNumberOfFollowers()+1);
        userService.save(follower);
        userService.save(followed);
    }

    @Transactional
    public void unfollowUser(Long id) {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User follower = userService.findByEmail(loggedInUser.getUsername()).get();

        User followed = userService.findById(id);

        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new IllegalStateException("Not following this user"));

        followRepository.delete(follow);

        follower.setNumberOfFollowing(follower.getNumberOfFollowing()-1);
        followed.setNumberOfFollowers(followed.getNumberOfFollowers()-1);
        userService.save(follower);
        userService.save(followed);
    }

    public boolean isFollowed(User follower, User followed){
        return followRepository.findByFollowerAndFollowed(follower, followed).isPresent();
    }
}
