package com.bcom.icms.repos;

import com.bcom.icms.domain.PriorityMatrix;
import com.bcom.icms.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PriorityMatrixRepository extends JpaRepository<PriorityMatrix, Long> {

    Page<PriorityMatrix> findAllById(Long id, Pageable pageable);

    PriorityMatrix findFirstByType(Type type);

}
