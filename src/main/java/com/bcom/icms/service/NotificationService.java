package com.bcom.icms.service;

import com.bcom.icms.model.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface NotificationService {

    Page<NotificationDTO> findAll(String filter, Pageable pageable);

    NotificationDTO get(Long id);

    Long create(NotificationDTO notificationDTO);

    void update(Long id, NotificationDTO notificationDTO);

    void delete(Long id);

}
