package com.bcom.icms.service;

import com.bcom.icms.model.ModuleSatusOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ModuleSatusOrderService {

    Page<ModuleSatusOrderDTO> findAll(String filter, Pageable pageable);

    ModuleSatusOrderDTO get(Long id);

    Long create(ModuleSatusOrderDTO moduleSatusOrderDTO);

    void update(Long id, ModuleSatusOrderDTO moduleSatusOrderDTO);

    void delete(Long id);

}
