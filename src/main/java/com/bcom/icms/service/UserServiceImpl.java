package com.bcom.icms.service;

import com.bcom.icms.domain.Comment;
import com.bcom.icms.domain.Notification;
import com.bcom.icms.domain.Ticket;
import com.bcom.icms.domain.User;
import com.bcom.icms.model.UserDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.CommentRepository;
import com.bcom.icms.repos.NotificationRepository;
import com.bcom.icms.repos.RoleRepository;
import com.bcom.icms.repos.TicketRepository;
import com.bcom.icms.repos.UserRepository;
import com.bcom.icms.util.NotFoundException;
import com.bcom.icms.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final UserMapper userMapper;
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    public UserServiceImpl(final UserRepository userRepository, final RoleRepository roleRepository,
            final ClientRepository clientRepository, final UserMapper userMapper,
            final TicketRepository ticketRepository, final CommentRepository commentRepository,
            final NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
        this.userMapper = userMapper;
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Page<UserDTO> findAll(final String filter, final Pageable pageable) {
        Page<User> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = userRepository.findAllById(longFilter, pageable);
        } else {
            page = userRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(user -> userMapper.updateUserDTO(user, new UserDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> userMapper.updateUserDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final UserDTO userDTO) {
        final User user = new User();
        userMapper.updateUser(userDTO, user, roleRepository, clientRepository);
        return userRepository.save(user).getId();
    }

    @Override
    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userMapper.updateUser(userDTO, user, roleRepository, clientRepository);
        userRepository.save(user);
    }

    @Override
    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Ticket assigneeTicket = ticketRepository.findFirstByAssignee(user);
        if (assigneeTicket != null) {
            referencedWarning.setKey("user.ticket.assignee.referenced");
            referencedWarning.addParam(assigneeTicket.getId());
            return referencedWarning;
        }
        final Comment userComment = commentRepository.findFirstByUser(user);
        if (userComment != null) {
            referencedWarning.setKey("user.comment.user.referenced");
            referencedWarning.addParam(userComment.getId());
            return referencedWarning;
        }
        final Notification userNotification = notificationRepository.findFirstByUser(user);
        if (userNotification != null) {
            referencedWarning.setKey("user.notification.user.referenced");
            referencedWarning.addParam(userNotification.getId());
            return referencedWarning;
        }
        return null;
    }

}
