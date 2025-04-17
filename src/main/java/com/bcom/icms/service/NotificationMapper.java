package com.bcom.icms.service;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Notification;
import com.bcom.icms.domain.Ticket;
import com.bcom.icms.domain.User;
import com.bcom.icms.model.NotificationDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.TicketRepository;
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
public interface NotificationMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    NotificationDTO updateNotificationDTO(Notification notification,
            @MappingTarget NotificationDTO notificationDTO);

    @AfterMapping
    default void afterUpdateNotificationDTO(Notification notification,
            @MappingTarget NotificationDTO notificationDTO) {
        notificationDTO.setClient(notification.getClient() == null ? null : notification.getClient().getId());
        notificationDTO.setUser(notification.getUser() == null ? null : notification.getUser().getId());
        notificationDTO.setTicket(notification.getTicket() == null ? null : notification.getTicket().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    Notification updateNotification(NotificationDTO notificationDTO,
            @MappingTarget Notification notification, @Context ClientRepository clientRepository,
            @Context UserRepository userRepository, @Context TicketRepository ticketRepository);

    @AfterMapping
    default void afterUpdateNotification(NotificationDTO notificationDTO,
            @MappingTarget Notification notification, @Context ClientRepository clientRepository,
            @Context UserRepository userRepository, @Context TicketRepository ticketRepository) {
        final Client client = notificationDTO.getClient() == null ? null : clientRepository.findById(notificationDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        notification.setClient(client);
        final User user = notificationDTO.getUser() == null ? null : userRepository.findById(notificationDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        notification.setUser(user);
        final Ticket ticket = notificationDTO.getTicket() == null ? null : ticketRepository.findById(notificationDTO.getTicket())
                .orElseThrow(() -> new NotFoundException("ticket not found"));
        notification.setTicket(ticket);
    }

}
