package com.example.security.post;

import com.example.security.auth.AuthenticationService;
import com.example.security.user.User;
import com.example.security.user.UserService;
import com.example.security.user.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;


    public void addPost(PostDtoRequest postDtoRequest) {
        var post = Post.builder()
                .content(postDtoRequest.getContent())
                .imageUrl(postDtoRequest.getImageUrl())
                .build();
        postRepository.save(post);
    }

    public List<PostDtoResponse> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> {
                    User user = userService.findById(post.getCreatedBy());
                    PostDtoResponse dto = new PostDtoResponse(
                            post.getId(),
                            post.getContent(),
                            post.getImageUrl(),
                            user.getId(),
                            user.getFirstname() + " " + user.getLastname(),
                            user.getImageUrl(),
                            post.getCreateDate().toString(),
                            post.getNumberOfLikes()
                    );
                    return dto;
                }
        ).collect(Collectors.toList());
    }

    public PostDtoResponse getPostById(Long id) {
        Post post = postRepository.findById(id).get();
        User user = userService.findById(post.getCreatedBy());//I get All attributes of user I want to retrieve only id , name , image ?

        return new PostDtoResponse(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                user.getId(),
                user.getFirstname() + " " + user.getLastname(),
                user.getImageUrl(),
                post.getCreateDate().toString(),
                post.getNumberOfLikes()
        );
    }

    public void update(PostDtoUpdate postDtoUpdate) {
        Post post = postRepository.findById(postDtoUpdate.getId()).get();
        System.out.println("hello");
        if (!authenticationService.getCurrentUser().get().getUsername().equals(userService.findById(post.getCreatedBy()).getEmail()))
            throw new UsernameNotFoundException("Wrong user try to update the product");
        post.setContent(postDtoUpdate.getContent());
        post.setImageUrl(postDtoUpdate.getImageUrl());
        postRepository.save(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).get();
        if (authenticationService.getCurrentUser().get().getUsername().equals(userService.findById(post.getCreatedBy()).getEmail()))
            postRepository.delete(post);
    }

    public void addPostToLiked(Long id) {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = (User) loggedInUser;
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));

        Set<User> userSet = null;
        userSet = post.getLikes();
        userSet.add(user);
        post.setLikes(userSet);
        post.setNumberOfLikes(userSet.size());
        postRepository.save(post);
    }

    public void deletePostFromLike(Long id) {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        unAssignUserFromProductLikes(user, post);
    }

    private void unAssignUserFromProductLikes(User user, Post post) {
        Set<User> userSet = null;
        userSet = post.getLikes();
        userSet.removeIf(item -> item.equals(user));
        post.setLikes(userSet);
        post.setNumberOfLikes(userSet.size());
        postRepository.save(post);

    }

    public Set<UserSummary> getUsersLikesPost(Long id) {
        Post post = postRepository.findById(id).get();
        Set<User> users = post.getLikes();
        return users.stream().map(
                user -> new UserSummary(user.getId(), user.getFirstname(), user.getImageUrl())
        ).collect(Collectors.toSet());
    }

    public List<PostDtoResponse> getPostsByJwt() {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();
        List<Post> posts = postRepository.findByCreatedBy(user.getId());
        return posts.stream().map(
                post -> new PostDtoResponse(
                        post.getId(),
                        post.getContent(),
                        post.getImageUrl(),
                        user.getId(),
                        user.getFirstname()+" "+user.getLastname(),
                        user.getImageUrl(),
                        post.getCreateDate().toString(),
                        post.getNumberOfLikes()
                )
        ).collect(Collectors.toList());
    }
    public List<PostDtoResponse> getPostsByUserId(Long id) {
        User user = userService.findById(id);
        List<Post> posts = postRepository.findByCreatedBy(id);
        return posts.stream().map(
                post -> new PostDtoResponse(
                        post.getId(),
                        post.getContent(),
                        post.getImageUrl(),
                        user.getId(),
                        user.getFirstname()+" "+user.getLastname(),
                        user.getImageUrl(),
                        post.getCreateDate().toString(),
                        post.getNumberOfLikes()
                )
        ).collect(Collectors.toList());
    }


}
