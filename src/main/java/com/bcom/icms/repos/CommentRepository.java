package com.bcom.icms.repos;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Comment;
import com.bcom.icms.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllById(Long id, Pageable pageable);

    Comment findFirstByClient(Client client);

    Comment findFirstByUser(User user);

}
