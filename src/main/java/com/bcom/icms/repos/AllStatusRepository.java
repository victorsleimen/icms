package com.bcom.icms.repos;

import com.bcom.icms.domain.AllStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AllStatusRepository extends JpaRepository<AllStatus, Long> {

    Page<AllStatus> findAllById(Long id, Pageable pageable);

}
