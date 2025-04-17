package com.bcom.icms.service;

import com.bcom.icms.domain.Attachment;
import com.bcom.icms.model.AttachmentDTO;
import com.bcom.icms.repos.AttachmentRepository;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.TypeRepository;
import com.bcom.icms.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final ClientRepository clientRepository;
    private final TypeRepository typeRepository;
    private final AttachmentMapper attachmentMapper;

    public AttachmentServiceImpl(final AttachmentRepository attachmentRepository,
            final ClientRepository clientRepository, final TypeRepository typeRepository,
            final AttachmentMapper attachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.clientRepository = clientRepository;
        this.typeRepository = typeRepository;
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    public Page<AttachmentDTO> findAll(final String filter, final Pageable pageable) {
        Page<Attachment> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = attachmentRepository.findAllById(longFilter, pageable);
        } else {
            page = attachmentRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(attachment -> attachmentMapper.updateAttachmentDTO(attachment, new AttachmentDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public AttachmentDTO get(final Long id) {
        return attachmentRepository.findById(id)
                .map(attachment -> attachmentMapper.updateAttachmentDTO(attachment, new AttachmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final AttachmentDTO attachmentDTO) {
        final Attachment attachment = new Attachment();
        attachmentMapper.updateAttachment(attachmentDTO, attachment, clientRepository, typeRepository);
        return attachmentRepository.save(attachment).getId();
    }

    @Override
    public void update(final Long id, final AttachmentDTO attachmentDTO) {
        final Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        attachmentMapper.updateAttachment(attachmentDTO, attachment, clientRepository, typeRepository);
        attachmentRepository.save(attachment);
    }

    @Override
    public void delete(final Long id) {
        attachmentRepository.deleteById(id);
    }

}
