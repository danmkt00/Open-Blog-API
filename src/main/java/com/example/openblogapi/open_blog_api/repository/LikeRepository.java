package com.example.openblogapi.open_blog_api.repository;

import com.example.openblogapi.open_blog_api.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPostId(Long postId);
    List<Like> findByPostIdOrderByCreatedAtAsc(Long postId);
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    Long countByPostId(Long postId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
