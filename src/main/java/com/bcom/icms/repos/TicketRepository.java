package com.bcom.icms.repos;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Sla;
import com.bcom.icms.domain.Ticket;
import com.bcom.icms.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findAllById(Long id, Pageable pageable);

    Ticket findFirstByClient(Client client);

    Ticket findFirstBySla(Sla sla);

    Ticket findFirstByAssignee(User user);

}
