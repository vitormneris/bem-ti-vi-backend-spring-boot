package com.bemtivi.bemtivi.controllers.in.comment.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.comment.Comment;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.comment.dto.CommentDTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CommentWebMapper {
    PageResponseDTO<CommentDTO> mapToPageResponseDto(PageResponse<Comment> pageResponse);
    Set<CommentDTO> mapToSetCommentDTO(Set<Comment> categories);
    CommentDTO mapToDTO(Comment comment);
    Comment mapToDomain(CommentDTO commentDTO);
}
