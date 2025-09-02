package com.springboot.messaging.dao;

import com.springboot.messaging.Entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface EmailRepository extends JpaRepository<Email, Long> {

    //all emails received by a user
    List<Email> findByReceiverId(Long receiverId);

    //all emails sent by a user
    List<Email> findBySenderId(Long senderId);

}
