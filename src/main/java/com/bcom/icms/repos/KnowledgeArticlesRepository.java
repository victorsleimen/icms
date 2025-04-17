package com.bcom.icms.repos;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.KnowledgeArticles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KnowledgeArticlesRepository extends JpaRepository<KnowledgeArticles, Long> {

    Page<KnowledgeArticles> findAllById(Long id, Pageable pageable);

    KnowledgeArticles findFirstByClient(Client client);

}
