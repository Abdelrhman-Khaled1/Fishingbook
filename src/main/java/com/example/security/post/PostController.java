package com.example.security.post;

import lombok.RequiredArgsConstructor;
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




}
