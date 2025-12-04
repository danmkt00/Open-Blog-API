package com.example.openblogapi.open_blog_api.mapper;


import com.example.openblogapi.open_blog_api.dto.CommentRequestDTO;
import com.example.openblogapi.open_blog_api.dto.CommentResponseDTO;
import com.example.openblogapi.open_blog_api.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    // Convert CommentRequestDTO → Comment entity
    public Comment toEntity(CommentRequestDTO commentRequestDTO) {
        Comment comment = new Comment();
        comment.setContent(commentRequestDTO.getContent());
        return comment;
    }

    // Convert Comment entity → CommentResponseDTO
    public CommentResponseDTO toDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthorUsername(comment.getUser().getUsername());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
