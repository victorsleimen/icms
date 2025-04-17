package com.bcom.icms.service;

import com.bcom.icms.model.TypeDTO;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TypeService {

    Page<TypeDTO> findAll(String filter, Pageable pageable);

    TypeDTO get(Long id);

    Long create(TypeDTO typeDTO);

    void update(Long id, TypeDTO typeDTO);

    void delete(Long id);

    ReferencedWarning getReferencedWarning(Long id);

}
