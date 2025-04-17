package com.bcom.icms.service;

import com.bcom.icms.domain.Attachment;
import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Type;
import com.bcom.icms.model.AttachmentDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.TypeRepository;
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
public interface AttachmentMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "type", ignore = true)
    AttachmentDTO updateAttachmentDTO(Attachment attachment,
            @MappingTarget AttachmentDTO attachmentDTO);

    @AfterMapping
    default void afterUpdateAttachmentDTO(Attachment attachment,
            @MappingTarget AttachmentDTO attachmentDTO) {
        attachmentDTO.setClient(attachment.getClient() == null ? null : attachment.getClient().getId());
        attachmentDTO.setType(attachment.getType() == null ? null : attachment.getType().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "type", ignore = true)
    Attachment updateAttachment(AttachmentDTO attachmentDTO, @MappingTarget Attachment attachment,
            @Context ClientRepository clientRepository, @Context TypeRepository typeRepository);

    @AfterMapping
    default void afterUpdateAttachment(AttachmentDTO attachmentDTO,
            @MappingTarget Attachment attachment, @Context ClientRepository clientRepository,
            @Context TypeRepository typeRepository) {
        final Client client = attachmentDTO.getClient() == null ? null : clientRepository.findById(attachmentDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        attachment.setClient(client);
        final Type type = attachmentDTO.getType() == null ? null : typeRepository.findById(attachmentDTO.getType())
                .orElseThrow(() -> new NotFoundException("type not found"));
        attachment.setType(type);
    }

}
