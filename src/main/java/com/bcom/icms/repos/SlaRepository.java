package com.bcom.icms.repos;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Sla;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SlaRepository extends JpaRepository<Sla, Long> {

    Page<Sla> findAllById(Long id, Pageable pageable);

    Sla findFirstByClient(Client client);

}
