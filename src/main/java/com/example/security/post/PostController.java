package com.example.security.post;

import com.example.security.user.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
        return ResponseEntity.ok(postService.getAllPostsWithLikedFlag());
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

    @GetMapping("/getUsersLikesPost/{id}")
    public Set<UserSummary> getUsersLikesPost(@PathVariable Long id){
        return postService.getUsersLikesPost(id);
    }


    @GetMapping("/getPostsByJwt")
    public List<PostDtoResponse> getPostsByJwt(){
        return postService.getPostsByJwtWithLikedFlag();
    }
    @GetMapping("/getPostsByUserId/{id}")
    public List<PostDtoResponse> getPostsByUserId(@PathVariable Long id){
        return postService.getPostsByUserIdWithLikedFlag(id);
    }

    @PostMapping("/report/{id}")
    public ResponseEntity reportProduct(@PathVariable Long id){
        postService.reportPost(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reported")
    public List<ReportedPostDto> getReportedPosts() {
        return postService.getReportedPosts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/{postId}")
    public ResponseEntity adminDeletePost(@PathVariable Long postId) {
        postService.adminDeletePost(postId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/reports/delete/{postId}")
    public ResponseEntity adminDeleteAllReportsForPost(@PathVariable Long postId){
        postService.deleteAllReportsForPost(postId);
        return new ResponseEntity(HttpStatus.OK);
    }


}
