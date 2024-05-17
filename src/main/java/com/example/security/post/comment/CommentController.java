package com.example.security.post.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> addCommentToPost(@PathVariable Long postId, @RequestBody CommentDtoRequest commentDtoRequest) {
        commentService.addComment(postId, commentDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDtoResponse>> getCommentsOfPost(@PathVariable Long postId) {
        List<CommentDtoResponse> comments = commentService.getPostComments(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        return commentService.delete(commentId) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

}
