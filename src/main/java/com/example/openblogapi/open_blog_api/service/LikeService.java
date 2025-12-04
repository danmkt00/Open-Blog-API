package com.example.openblogapi.open_blog_api.service;

import com.example.openblogapi.open_blog_api.dto.LikeResponseDTO;
import com.example.openblogapi.open_blog_api.exception.ResourceNotFoundException;
import com.example.openblogapi.open_blog_api.model.Like;
import com.example.openblogapi.open_blog_api.model.Post;
import com.example.openblogapi.open_blog_api.model.User;
import com.example.openblogapi.open_blog_api.repository.LikeRepository;
import com.example.openblogapi.open_blog_api.repository.PostRepository;
import com.example.openblogapi.open_blog_api.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    private final LikeRepository likeRepo;
    private final PostRepository postRepo;

    public LikeService(LikeRepository likeRepo, PostRepository postRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
    }

    @Transactional
    public void toggleLike(Long postId) {
        User currentUser = SecurityUtils.getCurrentUser(); // throws if not authenticated

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Optional<Like> existingLike = likeRepo.findByUserIdAndPostId(currentUser.getId(), postId);

        if (existingLike.isPresent()) {
            likeRepo.delete(existingLike.get());
        } else {
            Like like = new Like();
            like.setPost(post);
            like.setUser(currentUser);
            likeRepo.save(like);
        }
    }

    public List<LikeResponseDTO> getLikesForPost(Long postId) {
        return likeRepo.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(like -> new LikeResponseDTO(
                        like.getUser().getUsername(),
                        like.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public long countLikes(Long postId) {
        return likeRepo.countByPostId(postId);
    }
}
