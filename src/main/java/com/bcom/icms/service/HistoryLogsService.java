package com.bcom.icms.service;

import com.bcom.icms.model.HistoryLogsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface HistoryLogsService {

    Page<HistoryLogsDTO> findAll(String filter, Pageable pageable);

    HistoryLogsDTO get(Long id);

    Long create(HistoryLogsDTO historyLogsDTO);

    void update(Long id, HistoryLogsDTO historyLogsDTO);

    void delete(Long id);

}
