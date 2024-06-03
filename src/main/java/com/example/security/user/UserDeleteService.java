package com.example.security.user;

import com.example.security.post.PostService;
import com.example.security.product.ProductService;
import com.example.security.user.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDeleteService {
    private final UserService userService;
    private final ProductService productService;
    private final PostService postService;
    private final FollowService followService;

    public void unAssignDataForUser(User user) {
        postService.deleteAllPostLikesForUser(user);
        postService.deleteReportsByUser(user);
        followService.deleteAllFollowingAndFollowers(user); //TODO:: update numberOfFollowersAndFollowing
        productService.deleteAllLikesThatIMadeForAllProducts(user);
        productService.deleteAllLikesForMyProducts(user);
        productService.deleteReportsByUser(user);
        userService.save(user);
    }

}
