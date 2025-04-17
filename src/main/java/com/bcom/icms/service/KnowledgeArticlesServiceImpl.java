package com.bcom.icms.service;

import com.bcom.icms.domain.KnowledgeArticles;
import com.bcom.icms.model.KnowledgeArticlesDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.KnowledgeArticlesRepository;
import com.bcom.icms.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class KnowledgeArticlesServiceImpl implements KnowledgeArticlesService {

    private final KnowledgeArticlesRepository knowledgeArticlesRepository;
    private final ClientRepository clientRepository;
    private final KnowledgeArticlesMapper knowledgeArticlesMapper;

    public KnowledgeArticlesServiceImpl(
            final KnowledgeArticlesRepository knowledgeArticlesRepository,
            final ClientRepository clientRepository,
            final KnowledgeArticlesMapper knowledgeArticlesMapper) {
        this.knowledgeArticlesRepository = knowledgeArticlesRepository;
        this.clientRepository = clientRepository;
        this.knowledgeArticlesMapper = knowledgeArticlesMapper;
    }

    @Override
    public Page<KnowledgeArticlesDTO> findAll(final String filter, final Pageable pageable) {
        Page<KnowledgeArticles> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = knowledgeArticlesRepository.findAllById(longFilter, pageable);
        } else {
            page = knowledgeArticlesRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(knowledgeArticles -> knowledgeArticlesMapper.updateKnowledgeArticlesDTO(knowledgeArticles, new KnowledgeArticlesDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public KnowledgeArticlesDTO get(final Long id) {
        return knowledgeArticlesRepository.findById(id)
                .map(knowledgeArticles -> knowledgeArticlesMapper.updateKnowledgeArticlesDTO(knowledgeArticles, new KnowledgeArticlesDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final KnowledgeArticlesDTO knowledgeArticlesDTO) {
        final KnowledgeArticles knowledgeArticles = new KnowledgeArticles();
        knowledgeArticlesMapper.updateKnowledgeArticles(knowledgeArticlesDTO, knowledgeArticles, clientRepository);
        return knowledgeArticlesRepository.save(knowledgeArticles).getId();
    }

    @Override
    public void update(final Long id, final KnowledgeArticlesDTO knowledgeArticlesDTO) {
        final KnowledgeArticles knowledgeArticles = knowledgeArticlesRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        knowledgeArticlesMapper.updateKnowledgeArticles(knowledgeArticlesDTO, knowledgeArticles, clientRepository);
        knowledgeArticlesRepository.save(knowledgeArticles);
    }

    @Override
    public void delete(final Long id) {
        knowledgeArticlesRepository.deleteById(id);
    }

}
