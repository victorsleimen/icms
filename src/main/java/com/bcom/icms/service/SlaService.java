package com.bcom.icms.service;

import com.bcom.icms.model.SlaDTO;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SlaService {

    Page<SlaDTO> findAll(String filter, Pageable pageable);

    SlaDTO get(Long id);

    Long create(SlaDTO slaDTO);

    void update(Long id, SlaDTO slaDTO);

    void delete(Long id);

    ReferencedWarning getReferencedWarning(Long id);

}
