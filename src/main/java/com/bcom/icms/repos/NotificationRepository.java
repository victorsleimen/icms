package com.bcom.icms.repos;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Notification;
import com.bcom.icms.domain.Ticket;
import com.bcom.icms.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllById(Long id, Pageable pageable);

    Notification findFirstByClient(Client client);

    Notification findFirstByUser(User user);

    Notification findFirstByTicket(Ticket ticket);

}
