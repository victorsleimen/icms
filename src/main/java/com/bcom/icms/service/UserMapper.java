package com.bcom.icms.service;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Role;
import com.bcom.icms.domain.User;
import com.bcom.icms.model.UserDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.RoleRepository;
import com.bcom.icms.util.NotFoundException;
import java.util.HashSet;
import java.util.List;
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
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "client", ignore = true)
    UserDTO updateUserDTO(User user, @MappingTarget UserDTO userDTO);

    @AfterMapping
    default void afterUpdateUserDTO(User user, @MappingTarget UserDTO userDTO) {
        userDTO.setRoles(user.getRoles().stream()
                .map(role -> role.getId())
                .toList());
        userDTO.setClient(user.getClient() == null ? null : user.getClient().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "client", ignore = true)
    User updateUser(UserDTO userDTO, @MappingTarget User user,
            @Context RoleRepository roleRepository, @Context ClientRepository clientRepository);

    @AfterMapping
    default void afterUpdateUser(UserDTO userDTO, @MappingTarget User user,
            @Context RoleRepository roleRepository, @Context ClientRepository clientRepository) {
        final List<Role> roles = roleRepository.findAllById(
                userDTO.getRoles() == null ? List.of() : userDTO.getRoles());
        if (roles.size() != (userDTO.getRoles() == null ? 0 : userDTO.getRoles().size())) {
            throw new NotFoundException("one of roles not found");
        }
        user.setRoles(new HashSet<>(roles));
        final Client client = userDTO.getClient() == null ? null : clientRepository.findById(userDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        user.setClient(client);
    }

}
