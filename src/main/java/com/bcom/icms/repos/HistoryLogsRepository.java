package com.bcom.icms.repos;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.HistoryLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoryLogsRepository extends JpaRepository<HistoryLogs, Long> {

    Page<HistoryLogs> findAllById(Long id, Pageable pageable);

    HistoryLogs findFirstByClient(Client client);

}
