package com.bcom.icms.service;

import com.bcom.icms.model.AllStatusDTO;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AllStatusService {

    Page<AllStatusDTO> findAll(String filter, Pageable pageable);

    AllStatusDTO get(Long id);

    Long create(AllStatusDTO allStatusDTO);

    void update(Long id, AllStatusDTO allStatusDTO);

    void delete(Long id);

    ReferencedWarning getReferencedWarning(Long id);

}
