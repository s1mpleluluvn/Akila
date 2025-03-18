package com.akila.service;

import com.akila.entity.AccountEntity;
import com.akila.entity.TransactionEntity;
import com.akila.entity.data.Transaction;
import com.akila.repository.AccountRepository;
import com.akila.repository.CustomerRepository;
import com.akila.repository.TransactionRepository;
import com.akila.type.TransactionStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final EntityManager entityManager;

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public TransactionService(CustomerRepository customerRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, EntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.entityManager = entityManager;
    }


    @Transactional
    public Transaction transferMoney(String userName, String fromAccount, String toAccount, BigDecimal amount, String description) {
        // Fetch accounts
        AccountEntity fromAccountEntity = accountRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        // Validate account ownership
        if (!StringUtils.equals(fromAccountEntity.getCustomer().getUserName(), userName)) {
            throw new IllegalArgumentException("Account does not belong to user");
        }
        AccountEntity toAccountEntity = accountRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        // Validate currency
        if (!fromAccountEntity.getCurrency().equals(toAccountEntity.getCurrency())) {
            throw new IllegalArgumentException("Accounts must use the same currency");
        }

        // Validate balance
        if (fromAccountEntity.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Perform transfer
        fromAccountEntity.setBalance(fromAccountEntity.getBalance().subtract(amount));
        toAccountEntity.setBalance(toAccountEntity.getBalance().add(amount));

        // Save updated accounts
        accountRepository.save(fromAccountEntity);
        accountRepository.save(toAccountEntity);

        // Record transaction
        TransactionEntity transaction = new TransactionEntity();
        transaction.setFromAccount(fromAccountEntity);
        transaction.setToAccount(toAccountEntity);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setCompletedAt(LocalDateTime.now());
        transaction.setFee(BigDecimal.ZERO);
        transaction.setStatus(TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction).toData();
    }

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber, Pageable pageable) {
        var cb = this.entityManager.getCriteriaBuilder();
        var query = cb.createQuery(TransactionEntity.class);
        var root = query.from(TransactionEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.or(
                cb.equal(root.get("fromAccount").get("accountNumber"), accountNumber),
                cb.equal(root.get("toAccount").get("accountNumber"), accountNumber)));


        CriteriaQuery<TransactionEntity> select;

        if (CollectionUtils.isEmpty(predicates)) {
            select = query
                    .orderBy(cb.desc(root.get("createdAt")))
                    .select(root);
        } else {
            select = query
                    .where(predicates.toArray(Predicate[]::new))
                    .orderBy(cb.desc(root.get("createdAt")))
                    .select(root);
        }

        var typedQuery = this.entityManager.createQuery(select)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        List<TransactionEntity> result = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList<>();
        }

        return result.stream().map(TransactionEntity::toData).collect(Collectors.toList());
    }
}
