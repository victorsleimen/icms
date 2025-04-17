package com.bcom.icms.service;

import com.bcom.icms.model.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CommentService {

    Page<CommentDTO> findAll(String filter, Pageable pageable);

    CommentDTO get(Long id);

    Long create(CommentDTO commentDTO);

    void update(Long id, CommentDTO commentDTO);

    void delete(Long id);

}
