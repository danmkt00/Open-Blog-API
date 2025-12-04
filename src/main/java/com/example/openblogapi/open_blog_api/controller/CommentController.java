package com.example.openblogapi.open_blog_api.controller;

import com.example.openblogapi.open_blog_api.dto.CommentRequestDTO;
import com.example.openblogapi.open_blog_api.dto.CommentResponseDTO;
import com.example.openblogapi.open_blog_api.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Add comment to a post
    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDTO dto) {
        CommentResponseDTO response = commentService.addComment(postId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all comments for a post
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable Long postId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    // Update comment (author or admin)
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDTO dto) {
        CommentResponseDTO updated = commentService.updateComment(commentId, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete comment (author or admin)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
