package com.bcom.icms.service;

import com.bcom.icms.domain.Sla;
import com.bcom.icms.domain.Ticket;
import com.bcom.icms.model.SlaDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.SlaRepository;
import com.bcom.icms.repos.TicketRepository;
import com.bcom.icms.util.NotFoundException;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class SlaServiceImpl implements SlaService {

    private final SlaRepository slaRepository;
    private final ClientRepository clientRepository;
    private final SlaMapper slaMapper;
    private final TicketRepository ticketRepository;

    public SlaServiceImpl(final SlaRepository slaRepository,
            final ClientRepository clientRepository, final SlaMapper slaMapper,
            final TicketRepository ticketRepository) {
        this.slaRepository = slaRepository;
        this.clientRepository = clientRepository;
        this.slaMapper = slaMapper;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Page<SlaDTO> findAll(final String filter, final Pageable pageable) {
        Page<Sla> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = slaRepository.findAllById(longFilter, pageable);
        } else {
            page = slaRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(sla -> slaMapper.updateSlaDTO(sla, new SlaDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public SlaDTO get(final Long id) {
        return slaRepository.findById(id)
                .map(sla -> slaMapper.updateSlaDTO(sla, new SlaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final SlaDTO slaDTO) {
        final Sla sla = new Sla();
        slaMapper.updateSla(slaDTO, sla, clientRepository);
        return slaRepository.save(sla).getId();
    }

    @Override
    public void update(final Long id, final SlaDTO slaDTO) {
        final Sla sla = slaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        slaMapper.updateSla(slaDTO, sla, clientRepository);
        slaRepository.save(sla);
    }

    @Override
    public void delete(final Long id) {
        slaRepository.deleteById(id);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Sla sla = slaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Ticket slaTicket = ticketRepository.findFirstBySla(sla);
        if (slaTicket != null) {
            referencedWarning.setKey("sla.ticket.sla.referenced");
            referencedWarning.addParam(slaTicket.getId());
            return referencedWarning;
        }
        return null;
    }

}
