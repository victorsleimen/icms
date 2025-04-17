package com.bcom.icms.service;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Sla;
import com.bcom.icms.model.SlaDTO;
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
public interface SlaMapper {

    @Mapping(target = "client", ignore = true)
    SlaDTO updateSlaDTO(Sla sla, @MappingTarget SlaDTO slaDTO);

    @AfterMapping
    default void afterUpdateSlaDTO(Sla sla, @MappingTarget SlaDTO slaDTO) {
        slaDTO.setClient(sla.getClient() == null ? null : sla.getClient().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    Sla updateSla(SlaDTO slaDTO, @MappingTarget Sla sla,
            @Context ClientRepository clientRepository);

    @AfterMapping
    default void afterUpdateSla(SlaDTO slaDTO, @MappingTarget Sla sla,
            @Context ClientRepository clientRepository) {
        final Client client = slaDTO.getClient() == null ? null : clientRepository.findById(slaDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        sla.setClient(client);
    }

}
