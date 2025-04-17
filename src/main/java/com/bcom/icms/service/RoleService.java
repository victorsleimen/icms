package com.bcom.icms.service;

import com.bcom.icms.model.RoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RoleService {

    Page<RoleDTO> findAll(String filter, Pageable pageable);

    RoleDTO get(Long id);

    Long create(RoleDTO roleDTO);

    void update(Long id, RoleDTO roleDTO);

    void delete(Long id);

    boolean nameExists(String name);

}
