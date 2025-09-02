package com.springboot.messaging.service;

import com.springboot.messaging.Entity.Email;
import com.springboot.messaging.Entity.User;
import com.springboot.messaging.dao.EmailRepository;
import com.springboot.messaging.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    public Email sendEmail(String senderUsername, String receiverEmail, String subject, String body) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + senderUsername));
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Email email = new Email();
        email.setSender(sender);
        email.setReceiver(receiver);
        email.setSubject(subject);
        email.setBody(body);
        email.setSentAt(LocalDateTime.now());

        return emailRepository.save(email);
    }

    public List<Email> getInbox(Long userId) {
        return emailRepository.findByReceiverId(userId);
    }

    public List<Email> getSentEmails(Long userId) {
        return emailRepository.findBySenderId(userId);
    }

    public List<Email> getReceivedEmails(Long receiverId) {
        return emailRepository.findByReceiverId(receiverId);
    }

    public Optional<Email> findById(Long id) {
        return emailRepository.findById(id);
    }



}
