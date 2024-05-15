package com.example.security.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody PostDtoRequest postDtoRequest){
        postService.addPost(postDtoRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<PostDtoResponse>> findAllPosts(){
        return ResponseEntity.ok(postService.findAll());
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody PostDtoUpdate postDtoUpdate){
        postService.update(postDtoUpdate);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        postService.delete(id);
        return ResponseEntity.accepted().build();
    }


    @PutMapping("/like/{id}")
    public ResponseEntity addPostToLiked(@PathVariable Long id){
        postService.addPostToLiked(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/dislike/{id}")
    public void deletePostFromLiked(@PathVariable Long id){
        postService.deletePostFromLike(id);
    }


}
