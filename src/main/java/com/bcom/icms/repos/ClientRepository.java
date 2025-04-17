package com.bcom.icms.repos;

import com.bcom.icms.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findAllById(Long id, Pageable pageable);

}
