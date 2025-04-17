package com.bcom.icms.repos;

import com.bcom.icms.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Page<Role> findAllById(Long id, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

}
