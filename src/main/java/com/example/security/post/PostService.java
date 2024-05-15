package com.example.security.post;

import com.example.security.auth.AuthenticationService;
import com.example.security.user.User;
import com.example.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
                            post.getCreateDate().toString()
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
                post.getCreateDate().toString()
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
}
