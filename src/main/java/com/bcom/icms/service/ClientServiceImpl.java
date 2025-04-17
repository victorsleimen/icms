package com.bcom.icms.service;

import com.bcom.icms.domain.Attachment;
import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Comment;
import com.bcom.icms.domain.HistoryLogs;
import com.bcom.icms.domain.KnowledgeArticles;
import com.bcom.icms.domain.Notification;
import com.bcom.icms.domain.Sla;
import com.bcom.icms.domain.Ticket;
import com.bcom.icms.domain.User;
import com.bcom.icms.model.ClientDTO;
import com.bcom.icms.repos.AttachmentRepository;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.CommentRepository;
import com.bcom.icms.repos.HistoryLogsRepository;
import com.bcom.icms.repos.KnowledgeArticlesRepository;
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
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final SlaRepository slaRepository;
    private final AttachmentRepository attachmentRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final KnowledgeArticlesRepository knowledgeArticlesRepository;
    private final HistoryLogsRepository historyLogsRepository;

    public ClientServiceImpl(final ClientRepository clientRepository,
            final ClientMapper clientMapper, final UserRepository userRepository,
            final TicketRepository ticketRepository, final SlaRepository slaRepository,
            final AttachmentRepository attachmentRepository,
            final CommentRepository commentRepository,
            final NotificationRepository notificationRepository,
            final KnowledgeArticlesRepository knowledgeArticlesRepository,
            final HistoryLogsRepository historyLogsRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.slaRepository = slaRepository;
        this.attachmentRepository = attachmentRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
        this.knowledgeArticlesRepository = knowledgeArticlesRepository;
        this.historyLogsRepository = historyLogsRepository;
    }

    @Override
    public Page<ClientDTO> findAll(final String filter, final Pageable pageable) {
        Page<Client> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = clientRepository.findAllById(longFilter, pageable);
        } else {
            page = clientRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(client -> clientMapper.updateClientDTO(client, new ClientDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public ClientDTO get(final Long id) {
        return clientRepository.findById(id)
                .map(client -> clientMapper.updateClientDTO(client, new ClientDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final ClientDTO clientDTO) {
        final Client client = new Client();
        clientMapper.updateClient(clientDTO, client);
        return clientRepository.save(client).getId();
    }

    @Override
    public void update(final Long id, final ClientDTO clientDTO) {
        final Client client = clientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        clientMapper.updateClient(clientDTO, client);
        clientRepository.save(client);
    }

    @Override
    public void delete(final Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Client client = clientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User clientUser = userRepository.findFirstByClient(client);
        if (clientUser != null) {
            referencedWarning.setKey("client.user.client.referenced");
            referencedWarning.addParam(clientUser.getId());
            return referencedWarning;
        }
        final Ticket clientTicket = ticketRepository.findFirstByClient(client);
        if (clientTicket != null) {
            referencedWarning.setKey("client.ticket.client.referenced");
            referencedWarning.addParam(clientTicket.getId());
            return referencedWarning;
        }
        final Sla clientSla = slaRepository.findFirstByClient(client);
        if (clientSla != null) {
            referencedWarning.setKey("client.sla.client.referenced");
            referencedWarning.addParam(clientSla.getId());
            return referencedWarning;
        }
        final Attachment clientAttachment = attachmentRepository.findFirstByClient(client);
        if (clientAttachment != null) {
            referencedWarning.setKey("client.attachment.client.referenced");
            referencedWarning.addParam(clientAttachment.getId());
            return referencedWarning;
        }
        final Comment clientComment = commentRepository.findFirstByClient(client);
        if (clientComment != null) {
            referencedWarning.setKey("client.comment.client.referenced");
            referencedWarning.addParam(clientComment.getId());
            return referencedWarning;
        }
        final Notification clientNotification = notificationRepository.findFirstByClient(client);
        if (clientNotification != null) {
            referencedWarning.setKey("client.notification.client.referenced");
            referencedWarning.addParam(clientNotification.getId());
            return referencedWarning;
        }
        final KnowledgeArticles clientKnowledgeArticles = knowledgeArticlesRepository.findFirstByClient(client);
        if (clientKnowledgeArticles != null) {
            referencedWarning.setKey("client.knowledgeArticles.client.referenced");
            referencedWarning.addParam(clientKnowledgeArticles.getId());
            return referencedWarning;
        }
        final HistoryLogs clientHistoryLogs = historyLogsRepository.findFirstByClient(client);
        if (clientHistoryLogs != null) {
            referencedWarning.setKey("client.historyLogs.client.referenced");
            referencedWarning.addParam(clientHistoryLogs.getId());
            return referencedWarning;
        }
        return null;
    }

}
