/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.service;

import com.akila.entity.AccountEntity;
import com.akila.entity.TransactionEntity;
import com.akila.entity.data.Transaction;
import com.akila.repository.AccountRepository;
import com.akila.repository.CustomerRepository;
import com.akila.repository.TransactionRepository;
import com.akila.type.CurrencyType;
import com.akila.type.TransactionStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Minh
 */
@SpringBootTest
public class TransactionServiceTest {


    @Mock
    private EntityManager entityManager;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<TransactionEntity> criteriaQuery;

    @Mock
    private Root<TransactionEntity> root;

    @Mock
    private TypedQuery<TransactionEntity> typedQuery;

    private TransactionService transactionService;


    private AccountEntity fromAccount;
    private AccountEntity toAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(customerRepository, accountRepository, transactionRepository, entityManager);
        fromAccount = new AccountEntity();
        fromAccount.setAccountNumber("ACC001");
        fromAccount.setBalance(new BigDecimal("1000.00"));
        fromAccount.setCurrency(CurrencyType.USD);

        toAccount = new AccountEntity();
        toAccount.setAccountNumber("ACC002");
        toAccount.setBalance(new BigDecimal("500.00"));
        toAccount.setCurrency(CurrencyType.USD);
    }

    // Tests for transferMoney
    @Test
    void transferMoney_successfulTransfer() {
        // Arrange
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("ACC002")).thenReturn(Optional.of(toAccount));
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setFromAccount(fromAccount);
        transactionEntity.setToAccount(toAccount);
        transactionEntity.setAmount(new BigDecimal("200.00"));
        transactionEntity.setDescription("Test transfer");
        transactionEntity.setCompletedAt(LocalDateTime.now());
        transactionEntity.setFee(BigDecimal.ZERO);
        transactionEntity.setStatus(TransactionStatus.SUCCESS);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        Transaction result = transactionService.transferMoney("ACC001", "ACC002", new BigDecimal("200.00"), "Test transfer");

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("800.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());
        verify(accountRepository, times(2)).save(any(AccountEntity.class));
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    void transferMoney_sourceAccountNotFound_throwsException() {
        // Arrange
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                transactionService.transferMoney("ACC001", "ACC002", new BigDecimal("200.00"), "Test transfer"));
        assertEquals("Source account not found", exception.getMessage());
    }

    @Test
    void transferMoney_destinationAccountNotFound_throwsException() {
        // Arrange
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("ACC002")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                transactionService.transferMoney("ACC001", "ACC002", new BigDecimal("200.00"), "Test transfer"));
        assertEquals("Destination account not found", exception.getMessage());
    }

    @Test
    void transferMoney_differentCurrencies_throwsException() {
        // Arrange
        toAccount.setCurrency(CurrencyType.EUR);
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("ACC002")).thenReturn(Optional.of(toAccount));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.transferMoney("ACC001", "ACC002", new BigDecimal("200.00"), "Test transfer"));
        assertEquals("Accounts must use the same currency", exception.getMessage());
    }

    @Test
    void transferMoney_insufficientFunds_throwsException() {
        // Arrange
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("ACC002")).thenReturn(Optional.of(toAccount));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.transferMoney("ACC001", "ACC002", new BigDecimal("2000.00"), "Test transfer"));
        assertEquals("Insufficient funds", exception.getMessage());
    }

    // Tests for getTransactionsByAccountNumber
    @Test
    void getTransactionsByAccountNumber_returnsTransactions() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Mock CriteriaBuilder and CriteriaQuery
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(TransactionEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(TransactionEntity.class)).thenReturn(root);

        // Mock path navigation
        Path<Object> fromAccountPath = mock(Path.class);
        Path<Object> toAccountPath = mock(Path.class);
        when(root.get("fromAccount")).thenReturn(fromAccountPath);
        when(root.get("toAccount")).thenReturn(toAccountPath);
        when(fromAccountPath.get("accountNumber")).thenReturn(mock(Path.class));
        when(toAccountPath.get("accountNumber")).thenReturn(mock(Path.class));

        // Mock predicates and fluent API chain
        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.or(any(), any())).thenReturn(predicate);
        when(criteriaBuilder.desc(any())).thenReturn(mock(Order.class));

        // Fix: Ensure where() and orderBy() return the same CriteriaQuery instance
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(criteriaQuery.orderBy(any(Order.class))).thenReturn(criteriaQuery);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);

        // Mock TypedQuery
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult((int) pageable.getOffset())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(pageable.getPageSize())).thenReturn(typedQuery);

        // Mock result
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setFromAccount(fromAccount);
        transactionEntity.setToAccount(toAccount);
        transactionEntity.setAmount(new BigDecimal("200.00"));
        transactionEntity.setCompletedAt(LocalDateTime.now());
        transactionEntity.setStatus(TransactionStatus.SUCCESS);
        List<TransactionEntity> entities = Collections.singletonList(transactionEntity);
        when(typedQuery.getResultList()).thenReturn(entities);


        // Act
        List<Transaction> result = transactionService.getTransactionsByAccountNumber("ACC001", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(typedQuery, times(1)).getResultList();
    }
}
