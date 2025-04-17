package com.bcom.icms.service;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Sla;
import com.bcom.icms.domain.Ticket;
import com.bcom.icms.domain.User;
import com.bcom.icms.model.TicketDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.SlaRepository;
import com.bcom.icms.repos.UserRepository;
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
public interface TicketMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "sla", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    TicketDTO updateTicketDTO(Ticket ticket, @MappingTarget TicketDTO ticketDTO);

    @AfterMapping
    default void afterUpdateTicketDTO(Ticket ticket, @MappingTarget TicketDTO ticketDTO) {
        ticketDTO.setClient(ticket.getClient() == null ? null : ticket.getClient().getId());
        ticketDTO.setSla(ticket.getSla() == null ? null : ticket.getSla().getId());
        ticketDTO.setAssignee(ticket.getAssignee() == null ? null : ticket.getAssignee().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "sla", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    Ticket updateTicket(TicketDTO ticketDTO, @MappingTarget Ticket ticket,
            @Context ClientRepository clientRepository, @Context SlaRepository slaRepository,
            @Context UserRepository userRepository);

    @AfterMapping
    default void afterUpdateTicket(TicketDTO ticketDTO, @MappingTarget Ticket ticket,
            @Context ClientRepository clientRepository, @Context SlaRepository slaRepository,
            @Context UserRepository userRepository) {
        final Client client = ticketDTO.getClient() == null ? null : clientRepository.findById(ticketDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        ticket.setClient(client);
        final Sla sla = ticketDTO.getSla() == null ? null : slaRepository.findById(ticketDTO.getSla())
                .orElseThrow(() -> new NotFoundException("sla not found"));
        ticket.setSla(sla);
        final User assignee = ticketDTO.getAssignee() == null ? null : userRepository.findById(ticketDTO.getAssignee())
                .orElseThrow(() -> new NotFoundException("assignee not found"));
        ticket.setAssignee(assignee);
    }

}
