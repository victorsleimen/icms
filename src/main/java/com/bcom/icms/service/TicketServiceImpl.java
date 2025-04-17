package com.bcom.icms.service;

import com.bcom.icms.domain.Notification;
import com.bcom.icms.domain.Ticket;
import com.bcom.icms.model.TicketDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.NotificationRepository;
import com.bcom.icms.repos.SlaRepository;
import com.bcom.icms.repos.TicketRepository;
import com.bcom.icms.repos.UserRepository;
import com.bcom.icms.util.NotFoundException;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ClientRepository clientRepository;
    private final SlaRepository slaRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;
    private final NotificationRepository notificationRepository;

    public TicketServiceImpl(final TicketRepository ticketRepository,
            final ClientRepository clientRepository, final SlaRepository slaRepository,
            final UserRepository userRepository, final TicketMapper ticketMapper,
            final NotificationRepository notificationRepository) {
        this.ticketRepository = ticketRepository;
        this.clientRepository = clientRepository;
        this.slaRepository = slaRepository;
        this.userRepository = userRepository;
        this.ticketMapper = ticketMapper;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Page<TicketDTO> findAll(final String filter, final Pageable pageable) {
        Page<Ticket> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = ticketRepository.findAllById(longFilter, pageable);
        } else {
            page = ticketRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(ticket -> ticketMapper.updateTicketDTO(ticket, new TicketDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public TicketDTO get(final Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> ticketMapper.updateTicketDTO(ticket, new TicketDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final TicketDTO ticketDTO) {
        final Ticket ticket = new Ticket();
        ticketMapper.updateTicket(ticketDTO, ticket, clientRepository, slaRepository, userRepository);
        return ticketRepository.save(ticket).getId();
    }

    @Override
    public void update(final Long id, final TicketDTO ticketDTO) {
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        ticketMapper.updateTicket(ticketDTO, ticket, clientRepository, slaRepository, userRepository);
        ticketRepository.save(ticket);
    }

    @Override
    public void delete(final Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Notification ticketNotification = notificationRepository.findFirstByTicket(ticket);
        if (ticketNotification != null) {
            referencedWarning.setKey("ticket.notification.ticket.referenced");
            referencedWarning.addParam(ticketNotification.getId());
            return referencedWarning;
        }
        return null;
    }

}
