package com.bcom.icms.service;

import com.bcom.icms.domain.PriorityMatrix;
import com.bcom.icms.domain.Type;
import com.bcom.icms.model.PriorityMatrixDTO;
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
public interface PriorityMatrixMapper {

    @Mapping(target = "type", ignore = true)
    PriorityMatrixDTO updatePriorityMatrixDTO(PriorityMatrix priorityMatrix,
            @MappingTarget PriorityMatrixDTO priorityMatrixDTO);

    @AfterMapping
    default void afterUpdatePriorityMatrixDTO(PriorityMatrix priorityMatrix,
            @MappingTarget PriorityMatrixDTO priorityMatrixDTO) {
        priorityMatrixDTO.setType(priorityMatrix.getType() == null ? null : priorityMatrix.getType().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    PriorityMatrix updatePriorityMatrix(PriorityMatrixDTO priorityMatrixDTO,
            @MappingTarget PriorityMatrix priorityMatrix, @Context TypeRepository typeRepository);

    @AfterMapping
    default void afterUpdatePriorityMatrix(PriorityMatrixDTO priorityMatrixDTO,
            @MappingTarget PriorityMatrix priorityMatrix, @Context TypeRepository typeRepository) {
        final Type type = priorityMatrixDTO.getType() == null ? null : typeRepository.findById(priorityMatrixDTO.getType())
                .orElseThrow(() -> new NotFoundException("type not found"));
        priorityMatrix.setType(type);
    }

}
