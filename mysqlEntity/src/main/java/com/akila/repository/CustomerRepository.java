package com.akila.repository;

import com.akila.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByUserName(String userName);

    Optional<CustomerEntity> findByEmail(String email);
}
