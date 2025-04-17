package com.bcom.icms.repos;

import com.bcom.icms.domain.AllStatus;
import com.bcom.icms.domain.ModuleSatusOrder;
import com.bcom.icms.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ModuleSatusOrderRepository extends JpaRepository<ModuleSatusOrder, Long> {

    Page<ModuleSatusOrder> findAllById(Long id, Pageable pageable);

    ModuleSatusOrder findFirstByType(Type type);

    ModuleSatusOrder findFirstByStatus(AllStatus allStatus);

}
