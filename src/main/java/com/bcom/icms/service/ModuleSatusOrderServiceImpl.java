package com.bcom.icms.service;

import com.bcom.icms.domain.ModuleSatusOrder;
import com.bcom.icms.model.ModuleSatusOrderDTO;
import com.bcom.icms.repos.AllStatusRepository;
import com.bcom.icms.repos.ModuleSatusOrderRepository;
import com.bcom.icms.repos.TypeRepository;
import com.bcom.icms.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ModuleSatusOrderServiceImpl implements ModuleSatusOrderService {

    private final ModuleSatusOrderRepository moduleSatusOrderRepository;
    private final TypeRepository typeRepository;
    private final AllStatusRepository allStatusRepository;
    private final ModuleSatusOrderMapper moduleSatusOrderMapper;

    public ModuleSatusOrderServiceImpl(final ModuleSatusOrderRepository moduleSatusOrderRepository,
            final TypeRepository typeRepository, final AllStatusRepository allStatusRepository,
            final ModuleSatusOrderMapper moduleSatusOrderMapper) {
        this.moduleSatusOrderRepository = moduleSatusOrderRepository;
        this.typeRepository = typeRepository;
        this.allStatusRepository = allStatusRepository;
        this.moduleSatusOrderMapper = moduleSatusOrderMapper;
    }

    @Override
    public Page<ModuleSatusOrderDTO> findAll(final String filter, final Pageable pageable) {
        Page<ModuleSatusOrder> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = moduleSatusOrderRepository.findAllById(longFilter, pageable);
        } else {
            page = moduleSatusOrderRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(moduleSatusOrder -> moduleSatusOrderMapper.updateModuleSatusOrderDTO(moduleSatusOrder, new ModuleSatusOrderDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public ModuleSatusOrderDTO get(final Long id) {
        return moduleSatusOrderRepository.findById(id)
                .map(moduleSatusOrder -> moduleSatusOrderMapper.updateModuleSatusOrderDTO(moduleSatusOrder, new ModuleSatusOrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final ModuleSatusOrderDTO moduleSatusOrderDTO) {
        final ModuleSatusOrder moduleSatusOrder = new ModuleSatusOrder();
        moduleSatusOrderMapper.updateModuleSatusOrder(moduleSatusOrderDTO, moduleSatusOrder, typeRepository, allStatusRepository);
        return moduleSatusOrderRepository.save(moduleSatusOrder).getId();
    }

    @Override
    public void update(final Long id, final ModuleSatusOrderDTO moduleSatusOrderDTO) {
        final ModuleSatusOrder moduleSatusOrder = moduleSatusOrderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        moduleSatusOrderMapper.updateModuleSatusOrder(moduleSatusOrderDTO, moduleSatusOrder, typeRepository, allStatusRepository);
        moduleSatusOrderRepository.save(moduleSatusOrder);
    }

    @Override
    public void delete(final Long id) {
        moduleSatusOrderRepository.deleteById(id);
    }

}
