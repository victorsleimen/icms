package com.bcom.icms.service;

import com.bcom.icms.domain.Role;
import com.bcom.icms.model.RoleDTO;
import com.bcom.icms.repos.RoleRepository;
import com.bcom.icms.repos.UserRepository;
import com.bcom.icms.util.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;

    public RoleServiceImpl(final RoleRepository roleRepository, final RoleMapper roleMapper,
            final UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<RoleDTO> findAll(final String filter, final Pageable pageable) {
        Page<Role> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = roleRepository.findAllById(longFilter, pageable);
        } else {
            page = roleRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(role -> roleMapper.updateRoleDTO(role, new RoleDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public RoleDTO get(final Long id) {
        return roleRepository.findById(id)
                .map(role -> roleMapper.updateRoleDTO(role, new RoleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final RoleDTO roleDTO) {
        final Role role = new Role();
        roleMapper.updateRole(roleDTO, role);
        return roleRepository.save(role).getId();
    }

    @Override
    public void update(final Long id, final RoleDTO roleDTO) {
        final Role role = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roleMapper.updateRole(roleDTO, role);
        roleRepository.save(role);
    }

    @Override
    public void delete(final Long id) {
        final Role role = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByRoles(role)
                .forEach(user -> user.getRoles().remove(role));
        roleRepository.delete(role);
    }

    @Override
    public boolean nameExists(final String name) {
        return roleRepository.existsByNameIgnoreCase(name);
    }

}
