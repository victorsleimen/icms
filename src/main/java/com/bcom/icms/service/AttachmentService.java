package com.bcom.icms.service;

import com.bcom.icms.model.AttachmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AttachmentService {

    Page<AttachmentDTO> findAll(String filter, Pageable pageable);

    AttachmentDTO get(Long id);

    Long create(AttachmentDTO attachmentDTO);

    void update(Long id, AttachmentDTO attachmentDTO);

    void delete(Long id);

}
