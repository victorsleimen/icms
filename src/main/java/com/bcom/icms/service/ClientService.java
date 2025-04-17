package com.bcom.icms.service;

import com.bcom.icms.model.ClientDTO;
import com.bcom.icms.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClientService {

    Page<ClientDTO> findAll(String filter, Pageable pageable);

    ClientDTO get(Long id);

    Long create(ClientDTO clientDTO);

    void update(Long id, ClientDTO clientDTO);

    void delete(Long id);

    ReferencedWarning getReferencedWarning(Long id);

}
