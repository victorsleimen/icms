package com.bcom.icms.service;

import com.bcom.icms.domain.AllStatus;
import com.bcom.icms.domain.ModuleSatusOrder;
import com.bcom.icms.domain.Type;
import com.bcom.icms.model.ModuleSatusOrderDTO;
import com.bcom.icms.repos.AllStatusRepository;
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
public interface ModuleSatusOrderMapper {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "status", ignore = true)
    ModuleSatusOrderDTO updateModuleSatusOrderDTO(ModuleSatusOrder moduleSatusOrder,
            @MappingTarget ModuleSatusOrderDTO moduleSatusOrderDTO);

    @AfterMapping
    default void afterUpdateModuleSatusOrderDTO(ModuleSatusOrder moduleSatusOrder,
            @MappingTarget ModuleSatusOrderDTO moduleSatusOrderDTO) {
        moduleSatusOrderDTO.setType(moduleSatusOrder.getType() == null ? null : moduleSatusOrder.getType().getId());
        moduleSatusOrderDTO.setStatus(moduleSatusOrder.getStatus() == null ? null : moduleSatusOrder.getStatus().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "status", ignore = true)
    ModuleSatusOrder updateModuleSatusOrder(ModuleSatusOrderDTO moduleSatusOrderDTO,
            @MappingTarget ModuleSatusOrder moduleSatusOrder,
            @Context TypeRepository typeRepository,
            @Context AllStatusRepository allStatusRepository);

    @AfterMapping
    default void afterUpdateModuleSatusOrder(ModuleSatusOrderDTO moduleSatusOrderDTO,
            @MappingTarget ModuleSatusOrder moduleSatusOrder,
            @Context TypeRepository typeRepository,
            @Context AllStatusRepository allStatusRepository) {
        final Type type = moduleSatusOrderDTO.getType() == null ? null : typeRepository.findById(moduleSatusOrderDTO.getType())
                .orElseThrow(() -> new NotFoundException("type not found"));
        moduleSatusOrder.setType(type);
        final AllStatus status = moduleSatusOrderDTO.getStatus() == null ? null : allStatusRepository.findById(moduleSatusOrderDTO.getStatus())
                .orElseThrow(() -> new NotFoundException("status not found"));
        moduleSatusOrder.setStatus(status);
    }

}
