package com.bcom.icms.repos;

import com.bcom.icms.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TypeRepository extends JpaRepository<Type, Long> {

    Page<Type> findAllById(Long id, Pageable pageable);

}
