/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.service;

import com.akila.entity.AccountEntity;
import com.akila.entity.CustomerEntity;
import com.akila.type.AccountStatus;
import com.akila.entity.data.Customer;
import com.akila.type.CurrencyType;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.akila.repository.CustomerRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Minh
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;


    public CustomerService(CustomerRepository customerRepository, EntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.entityManager = entityManager;
    }


    @Transactional
    public void initAdmin(String userName, String passwordHash) {
        if (customerRepository.findByUserName(userName).isPresent()) {
            return;
        }
        CustomerEntity userEntity = new CustomerEntity();
        userEntity.setUserName(userName);
        userEntity.setPasswordHash(passwordHash);
        userEntity.setStatus(AccountStatus.ACTIVE);
        userEntity.setEmail(userName + "@gmail.com");
        userEntity.setActivatedAt(LocalDateTime.now());
        userEntity = customerRepository.save(userEntity);

        createAccount(userEntity, CurrencyType.USD);
        createAccount(userEntity, CurrencyType.VND);
    }

    //register
    @Transactional
    public Customer register(Customer user) {
        //check if user already exists
        if (customerRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        //check if email already exists
        if (customerRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        CustomerEntity userEntity = new CustomerEntity();
        userEntity.setUserName(user.getUserName());
        userEntity.setPasswordHash(user.getPasswordHash());
        userEntity.setGender(user.getGender());
        userEntity.setEmail(user.getEmail());
        userEntity.setAddress(user.getAddress());
        userEntity.setStatus(AccountStatus.ACTIVE);
        userEntity.setActivatedAt(user.getActivatedAt());
        userEntity = customerRepository.save(userEntity);

        createAccount(userEntity, CurrencyType.USD);
        createAccount(userEntity, CurrencyType.VND);
        return userEntity.toData();
    }

    @Transactional
    public void createAccount(CustomerEntity customerEntity, CurrencyType currencyType) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCustomer(customerEntity);
        accountEntity.setBalance(BigDecimal.valueOf(1000000));
        accountEntity.setStatus(AccountStatus.ACTIVE);
        accountEntity.setCurrency(currencyType);
        accountEntity.setAccountName(customerEntity.getUserName());
        entityManager.persist(accountEntity);
    }

    //get by email and username
    @Transactional
    public Customer findByUserName(String userName) {
        if (userName.contains("@")) {
            return customerRepository.findByEmail(userName)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .toData();
        } else {
            return customerRepository.findByUserName(userName)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .toData();
        }
    }

    //change password
    @Transactional
    public void changePassword(String userName, String newPassword) {
        CustomerEntity userEntity = customerRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setPasswordHash(newPassword);
        customerRepository.save(userEntity);
    }
}
