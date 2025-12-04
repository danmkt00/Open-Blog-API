package com.example.openblogapi.open_blog_api.controller;

import com.example.openblogapi.open_blog_api.dto.LikeResponseDTO;
import com.example.openblogapi.open_blog_api.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // Toggle like
    @PostMapping
    public ResponseEntity<Void> toggleLike(@PathVariable Long postId) {
        likeService.toggleLike(postId);
        return ResponseEntity.ok().build();
    }

    // Get all likes for a post
    @GetMapping
    public ResponseEntity<List<LikeResponseDTO>> getLikes(@PathVariable Long postId) {
        List<LikeResponseDTO> likes = likeService.getLikesForPost(postId);
        return ResponseEntity.ok(likes);
    }


}

