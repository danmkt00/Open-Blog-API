package com.example.openblogapi.open_blog_api.mapper;

import com.example.openblogapi.open_blog_api.dto.PostRequestDTO;
import com.example.openblogapi.open_blog_api.dto.PostResponseDTO;
import com.example.openblogapi.open_blog_api.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public Post toEntity(PostRequestDTO postRequestDTO) {
        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        return post;
    }

    public PostResponseDTO toDTO(Post post, long commentCount, long likeCount, boolean likedByCurrentUser) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorUsername(post.getUser().getUsername());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setCommentCount(commentCount);
        dto.setLikeCount(likeCount);
        dto.setLikedByCurrentUser(likedByCurrentUser);
        return dto;
    }
}
