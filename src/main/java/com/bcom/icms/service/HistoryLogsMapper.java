package com.bcom.icms.service;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.HistoryLogs;
import com.bcom.icms.model.HistoryLogsDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.util.NotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HistoryLogsMapper {

    @Mapping(target = "client", ignore = true)
    HistoryLogsDTO updateHistoryLogsDTO(HistoryLogs historyLogs,
            @MappingTarget HistoryLogsDTO historyLogsDTO);

    @AfterMapping
    default void afterUpdateHistoryLogsDTO(HistoryLogs historyLogs,
            @MappingTarget HistoryLogsDTO historyLogsDTO) {
        historyLogsDTO.setClient(historyLogs.getClient() == null ? null : historyLogs.getClient().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    HistoryLogs updateHistoryLogs(HistoryLogsDTO historyLogsDTO,
            @MappingTarget HistoryLogs historyLogs, @Context ClientRepository clientRepository);

    @AfterMapping
    default void afterUpdateHistoryLogs(HistoryLogsDTO historyLogsDTO,
            @MappingTarget HistoryLogs historyLogs, @Context ClientRepository clientRepository) {
        final Client client = historyLogsDTO.getClient() == null ? null : clientRepository.findById(historyLogsDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        historyLogs.setClient(client);
    }

}
