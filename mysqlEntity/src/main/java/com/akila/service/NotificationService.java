package com.akila.service;

import com.akila.entity.EmailSentEntity;
import com.akila.entity.NotificationEntity;
import com.akila.repository.CustomerRepository;
import com.akila.repository.EmailSentRepository;
import com.akila.repository.NotificationRepository;
import com.akila.type.EmailStatus;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class NotificationService {

    @Autowired
    private EntityManager entityManager;

    private final CustomerRepository customerRepository;

    private final NotificationRepository notificationRepository;

    private final EmailSentRepository emailSentRepository;


    public NotificationService(CustomerRepository customerRepository, NotificationRepository notificationRepository,
                               EmailSentRepository emailSentRepository) {
        this.customerRepository = customerRepository;
        this.notificationRepository = notificationRepository;
        this.emailSentRepository = emailSentRepository;
    }

    @Async("taskExecutor")
    @Transactional
    public void sendEmail(String email, String message) {
        var customer = customerRepository.findByEmail(email).orElse(null);

        var emailSentEntity = new EmailSentEntity();
        emailSentEntity.setSentFrom("akila@gmail.com");
        emailSentEntity.setContent("Change Balance");
        emailSentEntity.setCustomer(customer);
        emailSentEntity.setStatus(EmailStatus.SENT);
        emailSentRepository.save(emailSentEntity);
    }

    @Async("taskExecutor")
    @Transactional
    public void sendNotification(String email, String message) {
        var customer = customerRepository.findByEmail(email).orElse(null);
        var NofiEntity = new NotificationEntity();
        NofiEntity.setTitle("Change Balance");
        NofiEntity.setContent(message);
        NofiEntity.setCustomer(customer);
        notificationRepository.save(NofiEntity);
    }
}
