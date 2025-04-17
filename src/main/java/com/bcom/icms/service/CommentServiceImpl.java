package com.bcom.icms.service;

import com.bcom.icms.domain.Comment;
import com.bcom.icms.model.CommentDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.CommentRepository;
import com.bcom.icms.repos.UserRepository;
import com.bcom.icms.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(final CommentRepository commentRepository,
            final ClientRepository clientRepository, final UserRepository userRepository,
            final CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public Page<CommentDTO> findAll(final String filter, final Pageable pageable) {
        Page<Comment> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = commentRepository.findAllById(longFilter, pageable);
        } else {
            page = commentRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(comment -> commentMapper.updateCommentDTO(comment, new CommentDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public CommentDTO get(final Long id) {
        return commentRepository.findById(id)
                .map(comment -> commentMapper.updateCommentDTO(comment, new CommentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final CommentDTO commentDTO) {
        final Comment comment = new Comment();
        commentMapper.updateComment(commentDTO, comment, clientRepository, userRepository);
        return commentRepository.save(comment).getId();
    }

    @Override
    public void update(final Long id, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        commentMapper.updateComment(commentDTO, comment, clientRepository, userRepository);
        commentRepository.save(comment);
    }

    @Override
    public void delete(final Long id) {
        commentRepository.deleteById(id);
    }

}
