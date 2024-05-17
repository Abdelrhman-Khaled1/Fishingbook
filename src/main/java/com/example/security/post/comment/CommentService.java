package com.example.security.post.comment;

import com.example.security.auth.AuthenticationService;
import com.example.security.post.Post;
import com.example.security.post.PostRepository;
import com.example.security.user.User;
import com.example.security.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository ;
    private final PostRepository postRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public void addComment(Long id, CommentDtoRequest commentDtoRequest) {
        Post post = postRepository.findById(id).get();

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        var comment = Comment.builder()
                .content(commentDtoRequest.getContent())
                .user(user)
                .post(post)
                .build();
        commentRepository.save(comment);

        post.setNumberOfComments(post.getNumberOfComments()+1);
        postRepository.save(post);
    }

    public List<CommentDtoResponse> getPostComments(Long id) {

        Post post = postRepository.findById(id).get();

        List<Comment> comments = commentRepository.findByPostId(post.getId());
        return comments.stream()
                .map(comment -> CommentDtoResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .publisherId(comment.getUser().getId())
                        .publisherName(comment.getUser().getFirstname()+" "+comment.getUser().getLastname())
                        .publisherImageUrl(comment.getUser().getImageUrl())
                        .createdAt(comment.getCreateDate().toString())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long commentId){
        Comment comment = commentRepository.findById(commentId).get();

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        if (loggedInUser.getUsername().equals(comment.getUser().getEmail())){
            Post post = comment.getPost();
            post.setNumberOfComments(post.getNumberOfComments()-1);
            postRepository.save(post);
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
}
