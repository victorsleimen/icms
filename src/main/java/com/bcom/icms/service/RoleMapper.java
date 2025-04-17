package com.bcom.icms.service;

import com.bcom.icms.domain.Role;
import com.bcom.icms.model.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    RoleDTO updateRoleDTO(Role role, @MappingTarget RoleDTO roleDTO);

    @Mapping(target = "id", ignore = true)
    Role updateRole(RoleDTO roleDTO, @MappingTarget Role role);

}
