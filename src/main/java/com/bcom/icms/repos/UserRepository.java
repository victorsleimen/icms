package com.bcom.icms.repos;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Role;
import com.bcom.icms.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByLoggedUser(String loggedUser);

    Page<User> findAllById(Long id, Pageable pageable);

    User findFirstByRoles(Role role);

    User findFirstByClient(Client client);

    List<User> findAllByRoles(Role role);

    boolean existsByUsernameIgnoreCase(String username);

}
