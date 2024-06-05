package com.example.security.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByCreatedBy(Long id);

    @Query("SELECT p FROM Post p WHERE p.numberOfReports > 0 ORDER BY p.numberOfReports DESC")
    List<Post> findAllReportedPostsOrderByNumberOfReports();
}
