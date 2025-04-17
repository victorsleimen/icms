package com.bcom.icms.service;

import com.bcom.icms.domain.Notification;
import com.bcom.icms.model.NotificationDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.NotificationRepository;
import com.bcom.icms.repos.TicketRepository;
import com.bcom.icms.repos.UserRepository;
import com.bcom.icms.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(final NotificationRepository notificationRepository,
            final ClientRepository clientRepository, final UserRepository userRepository,
            final TicketRepository ticketRepository, final NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Page<NotificationDTO> findAll(final String filter, final Pageable pageable) {
        Page<Notification> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = notificationRepository.findAllById(longFilter, pageable);
        } else {
            page = notificationRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(notification -> notificationMapper.updateNotificationDTO(notification, new NotificationDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public NotificationDTO get(final Long id) {
        return notificationRepository.findById(id)
                .map(notification -> notificationMapper.updateNotificationDTO(notification, new NotificationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final NotificationDTO notificationDTO) {
        final Notification notification = new Notification();
        notificationMapper.updateNotification(notificationDTO, notification, clientRepository, userRepository, ticketRepository);
        return notificationRepository.save(notification).getId();
    }

    @Override
    public void update(final Long id, final NotificationDTO notificationDTO) {
        final Notification notification = notificationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        notificationMapper.updateNotification(notificationDTO, notification, clientRepository, userRepository, ticketRepository);
        notificationRepository.save(notification);
    }

    @Override
    public void delete(final Long id) {
        notificationRepository.deleteById(id);
    }

}
