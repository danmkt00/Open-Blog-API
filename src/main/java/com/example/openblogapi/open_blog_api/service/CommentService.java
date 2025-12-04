package com.example.openblogapi.open_blog_api.service;

import com.example.openblogapi.open_blog_api.dto.CommentRequestDTO;
import com.example.openblogapi.open_blog_api.dto.CommentResponseDTO;
import com.example.openblogapi.open_blog_api.exception.ResourceNotFoundException;
import com.example.openblogapi.open_blog_api.exception.UnauthorizedActionException;
import com.example.openblogapi.open_blog_api.mapper.CommentMapper;
import com.example.openblogapi.open_blog_api.model.Comment;
import com.example.openblogapi.open_blog_api.model.Post;
import com.example.openblogapi.open_blog_api.model.Role;
import com.example.openblogapi.open_blog_api.model.User;
import com.example.openblogapi.open_blog_api.repository.CommentRepository;
import com.example.openblogapi.open_blog_api.repository.PostRepository;
import com.example.openblogapi.open_blog_api.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepo, PostRepository postRepo, CommentMapper commentMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.commentMapper = commentMapper;
    }

    // Add a comment to a post
    public CommentResponseDTO addComment(Long postId, CommentRequestDTO dto) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        User currentUser = SecurityUtils.getCurrentUser();
        Comment comment = commentMapper.toEntity(dto);
        comment.setPost(post);
        comment.setUser(currentUser);

        commentRepo.save(comment);
        return commentMapper.toDTO(comment);
    }

    // Get all comments for a post
    public List<CommentResponseDTO> getCommentsByPost(Long postId) {
        return commentRepo.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }


    // Update a comment (author or admin)
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO dto) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        User currentUser = SecurityUtils.getCurrentUser();

        if (!comment.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("You are not allowed to update this comment");
        }

        comment.setContent(dto.getContent());
        commentRepo.save(comment);
        return commentMapper.toDTO(comment);
    }

    // Delete a comment (author or admin)
    public void deleteComment(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        User currentUser = SecurityUtils.getCurrentUser();

        if (!comment.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("You are not allowed to delete this comment");
        }

        commentRepo.delete(comment);
    }


}
