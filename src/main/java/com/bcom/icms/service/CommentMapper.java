package com.bcom.icms.service;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Comment;
import com.bcom.icms.domain.User;
import com.bcom.icms.model.CommentDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.UserRepository;
import com.bcom.icms.util.NotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommentMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "user", ignore = true)
    CommentDTO updateCommentDTO(Comment comment, @MappingTarget CommentDTO commentDTO);

    @AfterMapping
    default void afterUpdateCommentDTO(Comment comment, @MappingTarget CommentDTO commentDTO) {
        commentDTO.setClient(comment.getClient() == null ? null : comment.getClient().getId());
        commentDTO.setUser(comment.getUser() == null ? null : comment.getUser().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "user", ignore = true)
    Comment updateComment(CommentDTO commentDTO, @MappingTarget Comment comment,
            @Context ClientRepository clientRepository, @Context UserRepository userRepository);

    @AfterMapping
    default void afterUpdateComment(CommentDTO commentDTO, @MappingTarget Comment comment,
            @Context ClientRepository clientRepository, @Context UserRepository userRepository) {
        final Client client = commentDTO.getClient() == null ? null : clientRepository.findById(commentDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        comment.setClient(client);
        final User user = commentDTO.getUser() == null ? null : userRepository.findById(commentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        comment.setUser(user);
    }

}
