package com.akila.repository;

import com.akila.entity.EmailSentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSentRepository extends JpaRepository<EmailSentEntity, Long> {
}
