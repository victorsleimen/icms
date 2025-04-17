package com.bcom.icms.service;

import com.bcom.icms.domain.AllStatus;
import com.bcom.icms.model.AllStatusDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AllStatusMapper {

    AllStatusDTO updateAllStatusDTO(AllStatus allStatus, @MappingTarget AllStatusDTO allStatusDTO);

    @Mapping(target = "id", ignore = true)
    AllStatus updateAllStatus(AllStatusDTO allStatusDTO, @MappingTarget AllStatus allStatus);

}
