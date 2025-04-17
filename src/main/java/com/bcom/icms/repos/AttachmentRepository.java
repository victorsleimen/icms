package com.bcom.icms.repos;

import com.bcom.icms.domain.Attachment;
import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Page<Attachment> findAllById(Long id, Pageable pageable);

    Attachment findFirstByClient(Client client);

    Attachment findFirstByType(Type type);

}
