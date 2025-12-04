package com.example.openblogapi.open_blog_api.repository;

import com.example.openblogapi.open_blog_api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
}

