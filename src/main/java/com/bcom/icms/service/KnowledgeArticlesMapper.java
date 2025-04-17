package com.bcom.icms.service;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.KnowledgeArticles;
import com.bcom.icms.model.KnowledgeArticlesDTO;
import com.bcom.icms.repos.ClientRepository;
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
public interface KnowledgeArticlesMapper {

    @Mapping(target = "client", ignore = true)
    KnowledgeArticlesDTO updateKnowledgeArticlesDTO(KnowledgeArticles knowledgeArticles,
            @MappingTarget KnowledgeArticlesDTO knowledgeArticlesDTO);

    @AfterMapping
    default void afterUpdateKnowledgeArticlesDTO(KnowledgeArticles knowledgeArticles,
            @MappingTarget KnowledgeArticlesDTO knowledgeArticlesDTO) {
        knowledgeArticlesDTO.setClient(knowledgeArticles.getClient() == null ? null : knowledgeArticles.getClient().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    KnowledgeArticles updateKnowledgeArticles(KnowledgeArticlesDTO knowledgeArticlesDTO,
            @MappingTarget KnowledgeArticles knowledgeArticles,
            @Context ClientRepository clientRepository);

    @AfterMapping
    default void afterUpdateKnowledgeArticles(KnowledgeArticlesDTO knowledgeArticlesDTO,
            @MappingTarget KnowledgeArticles knowledgeArticles,
            @Context ClientRepository clientRepository) {
        final Client client = knowledgeArticlesDTO.getClient() == null ? null : clientRepository.findById(knowledgeArticlesDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        knowledgeArticles.setClient(client);
    }

}
