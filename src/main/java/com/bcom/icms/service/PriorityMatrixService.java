package com.bcom.icms.service;

import com.bcom.icms.model.PriorityMatrixDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PriorityMatrixService {

    Page<PriorityMatrixDTO> findAll(String filter, Pageable pageable);

    PriorityMatrixDTO get(Long id);

    Long create(PriorityMatrixDTO priorityMatrixDTO);

    void update(Long id, PriorityMatrixDTO priorityMatrixDTO);

    void delete(Long id);

}
