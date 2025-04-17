package com.bcom.icms.service;

import com.bcom.icms.model.TicketDTO;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TicketService {

    Page<TicketDTO> findAll(String filter, Pageable pageable);

    TicketDTO get(Long id);

    Long create(TicketDTO ticketDTO);

    void update(Long id, TicketDTO ticketDTO);

    void delete(Long id);

    ReferencedWarning getReferencedWarning(Long id);

}
