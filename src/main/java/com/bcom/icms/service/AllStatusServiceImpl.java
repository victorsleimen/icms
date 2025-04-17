package com.bcom.icms.service;

import com.bcom.icms.domain.AllStatus;
import com.bcom.icms.domain.ModuleSatusOrder;
import com.bcom.icms.model.AllStatusDTO;
import com.bcom.icms.repos.AllStatusRepository;
import com.bcom.icms.repos.ModuleSatusOrderRepository;
import com.bcom.icms.util.NotFoundException;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AllStatusServiceImpl implements AllStatusService {

    private final AllStatusRepository allStatusRepository;
    private final AllStatusMapper allStatusMapper;
    private final ModuleSatusOrderRepository moduleSatusOrderRepository;

    public AllStatusServiceImpl(final AllStatusRepository allStatusRepository,
            final AllStatusMapper allStatusMapper,
            final ModuleSatusOrderRepository moduleSatusOrderRepository) {
        this.allStatusRepository = allStatusRepository;
        this.allStatusMapper = allStatusMapper;
        this.moduleSatusOrderRepository = moduleSatusOrderRepository;
    }

    @Override
    public Page<AllStatusDTO> findAll(final String filter, final Pageable pageable) {
        Page<AllStatus> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = allStatusRepository.findAllById(longFilter, pageable);
        } else {
            page = allStatusRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(allStatus -> allStatusMapper.updateAllStatusDTO(allStatus, new AllStatusDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public AllStatusDTO get(final Long id) {
        return allStatusRepository.findById(id)
                .map(allStatus -> allStatusMapper.updateAllStatusDTO(allStatus, new AllStatusDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final AllStatusDTO allStatusDTO) {
        final AllStatus allStatus = new AllStatus();
        allStatusMapper.updateAllStatus(allStatusDTO, allStatus);
        return allStatusRepository.save(allStatus).getId();
    }

    @Override
    public void update(final Long id, final AllStatusDTO allStatusDTO) {
        final AllStatus allStatus = allStatusRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        allStatusMapper.updateAllStatus(allStatusDTO, allStatus);
        allStatusRepository.save(allStatus);
    }

    @Override
    public void delete(final Long id) {
        allStatusRepository.deleteById(id);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final AllStatus allStatus = allStatusRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ModuleSatusOrder statusModuleSatusOrder = moduleSatusOrderRepository.findFirstByStatus(allStatus);
        if (statusModuleSatusOrder != null) {
            referencedWarning.setKey("allStatus.moduleSatusOrder.status.referenced");
            referencedWarning.addParam(statusModuleSatusOrder.getId());
            return referencedWarning;
        }
        return null;
    }

}
