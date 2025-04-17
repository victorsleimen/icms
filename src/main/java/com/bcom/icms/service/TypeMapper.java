package com.bcom.icms.service;

import com.bcom.icms.domain.Type;
import com.bcom.icms.model.TypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TypeMapper {

    TypeDTO updateTypeDTO(Type type, @MappingTarget TypeDTO typeDTO);

    @Mapping(target = "id", ignore = true)
    Type updateType(TypeDTO typeDTO, @MappingTarget Type type);

}
