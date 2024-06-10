package com.example.security.post;

import com.example.security.auth.AuthenticationService;
import com.example.security.user.User;
import com.example.security.user.UserService;
import com.example.security.user.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;


    public void addPost(PostDtoRequest postDtoRequest) {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        var post = Post.builder()
                .content(postDtoRequest.getContent())
                .imageUrl(postDtoRequest.getImageUrl())
                .owner(user)
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
                            post.getNumberOfLikes(),
                            post.getNumberOfComments()
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
                post.getNumberOfLikes(),
                post.getNumberOfComments()
        );
    }

    public void update(PostDtoUpdate postDtoUpdate) {
        Post post = postRepository.findById(postDtoUpdate.getId()).get();
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
        unAssignUserFromPostLikes(user, post);
    }

    private void unAssignUserFromPostLikes(User user, Post post) {
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
                        user.getFirstname() + " " + user.getLastname(),
                        user.getImageUrl(),
                        post.getCreateDate().toString(),
                        post.getNumberOfLikes(),
                        post.getNumberOfComments()
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
                        user.getFirstname() + " " + user.getLastname(),
                        user.getImageUrl(),
                        post.getCreateDate().toString(),
                        post.getNumberOfLikes(),
                        post.getNumberOfComments()
                )
        ).collect(Collectors.toList());
    }

    public boolean isUserLikesPost(User user, Post post) {
        return post.getLikes().contains(user);
    }

    public List<PostDtoResponse> getAllPostsWithLikedFlag() {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        List<Post> posts = postRepository.findAll();

        List<PostDtoResponse> flaggedPosts = new ArrayList<>(posts.size());

        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            User owner = post.getOwner();
            boolean isUserLikesPost = isUserLikesPost(user, post);
            flaggedPosts.add(
                    new PostDtoResponse(
                            post.getId(),
                            post.getContent(),
                            post.getImageUrl(),
                            owner.getId(),
                            owner.getFirstname() + " " + owner.getLastname(),
                            owner.getImageUrl(),
                            post.getCreateDate().toString(),
                            post.getNumberOfLikes(),
                            post.getNumberOfComments(),
                            isUserLikesPost
                    )
            );
        }
        Collections.reverse(flaggedPosts);
        return flaggedPosts;
    }

    public List<PostDtoResponse> getPostsByJwtWithLikedFlag() {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        List<Post> posts = postRepository.findByCreatedBy(user.getId());

        List<PostDtoResponse> postsDto = posts.stream().map(
                post -> {
                    boolean isUserLikesPost = isUserLikesPost(user, post);
                    PostDtoResponse postDtoResponse = new PostDtoResponse(
                            post.getId(),
                            post.getContent(),
                            post.getImageUrl(),
                            user.getId(),
                            user.getFirstname() + " " + user.getLastname(),
                            user.getImageUrl(),
                            post.getCreateDate().toString(),
                            post.getNumberOfLikes(),
                            post.getNumberOfComments(),
                            isUserLikesPost
                    );
                    return postDtoResponse;
                }
        ).collect(Collectors.toList());
        Collections.reverse(postsDto);
        return postsDto;
    }

    public List<PostDtoResponse> getPostsByUserIdWithLikedFlag(Long id) {
        User user = userService.findById(id);
        List<Post> posts = postRepository.findByCreatedBy(id);


        List<PostDtoResponse> postsDto = posts.stream().map(
                post -> {
                    boolean isUserLikesPost = isUserLikesPost(user, post);
                    PostDtoResponse postDtoResponse = new PostDtoResponse(
                            post.getId(),
                            post.getContent(),
                            post.getImageUrl(),
                            user.getId(),
                            user.getFirstname() + " " + user.getLastname(),
                            user.getImageUrl(),
                            post.getCreateDate().toString(),
                            post.getNumberOfLikes(),
                            post.getNumberOfComments(),
                            isUserLikesPost
                    );
                    return postDtoResponse;
                }
        ).collect(Collectors.toList());
        Collections.reverse(postsDto);
        return postsDto;
    }


    public void deleteAllPostLikesForUser(User user) {
        List<Post> likedPosts = user.getLikedPosts().stream().toList();
        likedPosts.stream().forEach(post -> unAssignUserFromPostLikes(user, post));
        userService.save(user);
    }


    public void reportPost(Long id) {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        Post post = postRepository.findById(id).get();
        Set<User> reporters = post.getPost_reports();
        if (!reporters.contains(user)) {
            reporters.add(user);
            post.setPost_reports(reporters);
            post.setNumberOfReports(post.getNumberOfReports() + 1);
            postRepository.save(post);
        }
    }

    public void deleteReportsByUser(User user) {
        Set<Post> postsToReport = user.getPostsToReport();
        postsToReport.stream().forEach(post -> {
            post.setPost_reports(null);
            postRepository.save(post);
        });
        user.setPostsToReport(null);
        userService.save(user);
    }


    public boolean adminDeletePost(Long id) {
        Post post = postRepository.findById(id).get();

        Set<User> likedUsers = post.getLikes();
        likedUsers.stream()
                .forEach(user -> unAssignUserFromPostLikes(user, post));
        postRepository.deleteById(id);
        return true;
    }

    public List<ReportedPostDto> getReportedPosts() {
        return postRepository.findAllReportedPostsOrderByNumberOfReports().stream().map(
                post -> new ReportedPostDto(
                        post.getId(),
                        post.getContent(),
                        post.getImageUrl(),
                        post.getOwner().getId(),
                        post.getOwner().getFirstname(),
                        post.getOwner().getImageUrl(),
                        post.getCreateDate().toString(),
                        post.getNumberOfLikes(),
                        post.getNumberOfComments(),
                        post.getNumberOfReports()
                )).collect(Collectors.toList());
    }

    public boolean deleteAllReportsForPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (!optionalPost.isPresent()) {
            return false; // Or throw an exception, depending on your requirement
        }

        Post post = optionalPost.get();

        // Clear the post reports set and update the number of reports
        post.setPost_reports(new HashSet<>());
        post.setNumberOfReports(0);

        // Save the updated post
        postRepository.save(post);

        return true;
    }



}
