package com.example.openblogapi.open_blog_api.service;

import com.example.openblogapi.open_blog_api.dto.PostRequestDTO;
import com.example.openblogapi.open_blog_api.dto.PostResponseDTO;
import com.example.openblogapi.open_blog_api.exception.ResourceNotFoundException;
import com.example.openblogapi.open_blog_api.exception.UnauthorizedActionException;
import com.example.openblogapi.open_blog_api.mapper.PostMapper;
import com.example.openblogapi.open_blog_api.model.Post;
import com.example.openblogapi.open_blog_api.model.Role;
import com.example.openblogapi.open_blog_api.model.User;
import com.example.openblogapi.open_blog_api.repository.CommentRepository;
import com.example.openblogapi.open_blog_api.repository.LikeRepository;
import com.example.openblogapi.open_blog_api.repository.PostRepository;
import com.example.openblogapi.open_blog_api.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final LikeRepository likeRepo;
    private final PostMapper postMapper;


    public PostService(PostRepository postRepo, CommentRepository commentRepo, PostMapper postMapper, LikeRepository likeRepo) {
        this.postRepo = postRepo;
        this.likeRepo = likeRepo;
        this.commentRepo = commentRepo;
        this.postMapper = postMapper;
    }

    // Create a new post
    public PostResponseDTO createPost(PostRequestDTO dto) {
        User currentUser = getCurrentUser();

        Post post = postMapper.toEntity(dto);
        post.setUser(currentUser);
        postRepo.save(post);

        long commentCount = 0; // new post has no comments
        long likeCount = 0; //new post has no likes
        boolean likedByCurrentUser = false; //new post wasn't liked by user

        return postMapper.toDTO(post, commentCount, likeCount, likedByCurrentUser);
    }

    // Get all posts
    public List<PostResponseDTO> getAllPosts() {
        User currentUser = SecurityUtils.getCurrentUserOrNull(); // user can be anonymous

        return postRepo.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> {
                    long commentCount = commentRepo.countByPostId(post.getId());
                    long likeCount = likeRepo.countByPostId(post.getId());

                    boolean likedByCurrentUser = currentUser != null &&
                            likeRepo.existsByUserIdAndPostId(currentUser.getId(), post.getId());

                    return postMapper.toDTO(post, commentCount, likeCount, likedByCurrentUser);
                })
                .collect(Collectors.toList());
    }

    // Get post by ID
    public PostResponseDTO getPostById(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        long commentCount = commentRepo.countByPostId(post.getId());
        long likeCount = likeRepo.countByPostId(post.getId());

        User currentUser = SecurityUtils.getCurrentUserOrNull();

        boolean likedByCurrentUser = false;
        if (currentUser != null) {
            likedByCurrentUser = likeRepo.existsByUserIdAndPostId(currentUser.getId(), post.getId());
        }

        return postMapper.toDTO(post, commentCount, likeCount, likedByCurrentUser);
    }

    // Update a post (owner or admin)
    public PostResponseDTO updatePost(Long postId, PostRequestDTO dto) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        User currentUser = getCurrentUser();

        // Allow only post owner or admin
        if (!post.getUser().getId().equals(currentUser.getId())
                && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("You are not allowed to update this post");
        }

        // Update fields
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        postRepo.save(post);

        // Counts
        long commentCount = commentRepo.countByPostId(post.getId());
        long likeCount = likeRepo.countByPostId(post.getId());

        boolean likedByCurrentUser =
                likeRepo.existsByUserIdAndPostId(currentUser.getId(), post.getId());

        return postMapper.toDTO(post, commentCount, likeCount, likedByCurrentUser);
    }

    // Delete a post (owner or admin)
    public void deletePost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        User currentUser = SecurityUtils.getCurrentUser();

        if (!post.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("You are not allowed to delete this post");
        }

        postRepo.delete(post);
    }

    // Helper: get authenticated user
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Helper: get authenticated or anonymous user
    private User getCurrentUserOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return null; // anonymous guest
        }

        return (User) auth.getPrincipal();
    }

}
