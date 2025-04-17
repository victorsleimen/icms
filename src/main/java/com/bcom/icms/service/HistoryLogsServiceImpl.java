package com.bcom.icms.service;

import com.bcom.icms.domain.HistoryLogs;
import com.bcom.icms.model.HistoryLogsDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.HistoryLogsRepository;
import com.bcom.icms.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class HistoryLogsServiceImpl implements HistoryLogsService {

    private final HistoryLogsRepository historyLogsRepository;
    private final ClientRepository clientRepository;
    private final HistoryLogsMapper historyLogsMapper;

    public HistoryLogsServiceImpl(final HistoryLogsRepository historyLogsRepository,
            final ClientRepository clientRepository, final HistoryLogsMapper historyLogsMapper) {
        this.historyLogsRepository = historyLogsRepository;
        this.clientRepository = clientRepository;
        this.historyLogsMapper = historyLogsMapper;
    }

    @Override
    public Page<HistoryLogsDTO> findAll(final String filter, final Pageable pageable) {
        Page<HistoryLogs> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = historyLogsRepository.findAllById(longFilter, pageable);
        } else {
            page = historyLogsRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(historyLogs -> historyLogsMapper.updateHistoryLogsDTO(historyLogs, new HistoryLogsDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public HistoryLogsDTO get(final Long id) {
        return historyLogsRepository.findById(id)
                .map(historyLogs -> historyLogsMapper.updateHistoryLogsDTO(historyLogs, new HistoryLogsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final HistoryLogsDTO historyLogsDTO) {
        final HistoryLogs historyLogs = new HistoryLogs();
        historyLogsMapper.updateHistoryLogs(historyLogsDTO, historyLogs, clientRepository);
        return historyLogsRepository.save(historyLogs).getId();
    }

    @Override
    public void update(final Long id, final HistoryLogsDTO historyLogsDTO) {
        final HistoryLogs historyLogs = historyLogsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        historyLogsMapper.updateHistoryLogs(historyLogsDTO, historyLogs, clientRepository);
        historyLogsRepository.save(historyLogs);
    }

    @Override
    public void delete(final Long id) {
        historyLogsRepository.deleteById(id);
    }

}
