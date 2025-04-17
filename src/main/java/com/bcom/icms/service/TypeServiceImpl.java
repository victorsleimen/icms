package com.bcom.icms.service;

import com.bcom.icms.domain.Attachment;
import com.bcom.icms.domain.ModuleSatusOrder;
import com.bcom.icms.domain.PriorityMatrix;
import com.bcom.icms.domain.Type;
import com.bcom.icms.model.TypeDTO;
import com.bcom.icms.repos.AttachmentRepository;
import com.bcom.icms.repos.ModuleSatusOrderRepository;
import com.bcom.icms.repos.PriorityMatrixRepository;
import com.bcom.icms.repos.TypeRepository;
import com.bcom.icms.util.NotFoundException;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;
    private final ModuleSatusOrderRepository moduleSatusOrderRepository;
    private final PriorityMatrixRepository priorityMatrixRepository;
    private final AttachmentRepository attachmentRepository;

    public TypeServiceImpl(final TypeRepository typeRepository, final TypeMapper typeMapper,
            final ModuleSatusOrderRepository moduleSatusOrderRepository,
            final PriorityMatrixRepository priorityMatrixRepository,
            final AttachmentRepository attachmentRepository) {
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
        this.moduleSatusOrderRepository = moduleSatusOrderRepository;
        this.priorityMatrixRepository = priorityMatrixRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Page<TypeDTO> findAll(final String filter, final Pageable pageable) {
        Page<Type> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = typeRepository.findAllById(longFilter, pageable);
        } else {
            page = typeRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(type -> typeMapper.updateTypeDTO(type, new TypeDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public TypeDTO get(final Long id) {
        return typeRepository.findById(id)
                .map(type -> typeMapper.updateTypeDTO(type, new TypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final TypeDTO typeDTO) {
        final Type type = new Type();
        typeMapper.updateType(typeDTO, type);
        return typeRepository.save(type).getId();
    }

    @Override
    public void update(final Long id, final TypeDTO typeDTO) {
        final Type type = typeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        typeMapper.updateType(typeDTO, type);
        typeRepository.save(type);
    }

    @Override
    public void delete(final Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Type type = typeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ModuleSatusOrder typeModuleSatusOrder = moduleSatusOrderRepository.findFirstByType(type);
        if (typeModuleSatusOrder != null) {
            referencedWarning.setKey("type.moduleSatusOrder.type.referenced");
            referencedWarning.addParam(typeModuleSatusOrder.getId());
            return referencedWarning;
        }
        final PriorityMatrix typePriorityMatrix = priorityMatrixRepository.findFirstByType(type);
        if (typePriorityMatrix != null) {
            referencedWarning.setKey("type.priorityMatrix.type.referenced");
            referencedWarning.addParam(typePriorityMatrix.getId());
            return referencedWarning;
        }
        final Attachment typeAttachment = attachmentRepository.findFirstByType(type);
        if (typeAttachment != null) {
            referencedWarning.setKey("type.attachment.type.referenced");
            referencedWarning.addParam(typeAttachment.getId());
            return referencedWarning;
        }
        return null;
    }

}
