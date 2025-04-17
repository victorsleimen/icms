package com.bcom.icms.service;

import com.bcom.icms.domain.Client;
import com.bcom.icms.model.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClientMapper {

    ClientDTO updateClientDTO(Client client, @MappingTarget ClientDTO clientDTO);

    @Mapping(target = "id", ignore = true)
    Client updateClient(ClientDTO clientDTO, @MappingTarget Client client);

}
