package com.bcom.icms.service;

import com.bcom.icms.domain.PriorityMatrix;
import com.bcom.icms.model.PriorityMatrixDTO;
import com.bcom.icms.repos.PriorityMatrixRepository;
import com.bcom.icms.repos.TypeRepository;
import com.bcom.icms.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class PriorityMatrixServiceImpl implements PriorityMatrixService {

    private final PriorityMatrixRepository priorityMatrixRepository;
    private final TypeRepository typeRepository;
    private final PriorityMatrixMapper priorityMatrixMapper;

    public PriorityMatrixServiceImpl(final PriorityMatrixRepository priorityMatrixRepository,
            final TypeRepository typeRepository, final PriorityMatrixMapper priorityMatrixMapper) {
        this.priorityMatrixRepository = priorityMatrixRepository;
        this.typeRepository = typeRepository;
        this.priorityMatrixMapper = priorityMatrixMapper;
    }

    @Override
    public Page<PriorityMatrixDTO> findAll(final String filter, final Pageable pageable) {
        Page<PriorityMatrix> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = priorityMatrixRepository.findAllById(longFilter, pageable);
        } else {
            page = priorityMatrixRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(priorityMatrix -> priorityMatrixMapper.updatePriorityMatrixDTO(priorityMatrix, new PriorityMatrixDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public PriorityMatrixDTO get(final Long id) {
        return priorityMatrixRepository.findById(id)
                .map(priorityMatrix -> priorityMatrixMapper.updatePriorityMatrixDTO(priorityMatrix, new PriorityMatrixDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final PriorityMatrixDTO priorityMatrixDTO) {
        final PriorityMatrix priorityMatrix = new PriorityMatrix();
        priorityMatrixMapper.updatePriorityMatrix(priorityMatrixDTO, priorityMatrix, typeRepository);
        return priorityMatrixRepository.save(priorityMatrix).getId();
    }

    @Override
    public void update(final Long id, final PriorityMatrixDTO priorityMatrixDTO) {
        final PriorityMatrix priorityMatrix = priorityMatrixRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        priorityMatrixMapper.updatePriorityMatrix(priorityMatrixDTO, priorityMatrix, typeRepository);
        priorityMatrixRepository.save(priorityMatrix);
    }

    @Override
    public void delete(final Long id) {
        priorityMatrixRepository.deleteById(id);
    }

}
