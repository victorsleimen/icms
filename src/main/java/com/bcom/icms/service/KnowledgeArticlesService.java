package com.bcom.icms.service;

import com.bcom.icms.model.KnowledgeArticlesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface KnowledgeArticlesService {

    Page<KnowledgeArticlesDTO> findAll(String filter, Pageable pageable);

    KnowledgeArticlesDTO get(Long id);

    Long create(KnowledgeArticlesDTO knowledgeArticlesDTO);

    void update(Long id, KnowledgeArticlesDTO knowledgeArticlesDTO);

    void delete(Long id);

}
