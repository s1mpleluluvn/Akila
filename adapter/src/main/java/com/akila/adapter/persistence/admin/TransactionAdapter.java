package com.akila.adapter.persistence.admin;

import com.akila.admin.port.out.TransactionPort;
import com.akila.entity.data.Transaction;
import com.akila.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TransactionAdapter implements TransactionPort {

    @Autowired
    private TransactionService transactionService;

    @Override
    public Transaction transferMoney(String userName, String fromAccount, String toAccount, BigDecimal amount, String description) {
        return transactionService.transferMoney(userName, fromAccount, toAccount, amount, description);
    }

    @Override
    public List<Transaction> getTransactionsByAccountNumber(String userName, String accountNumber, Pageable pageable) {
        return transactionService.getTransactionsByAccountNumber(userName,accountNumber, pageable);
    }

    @Override
    public Long countTransactionsByAccountNumber(String userName, String accountNumber) {
        return transactionService.countTransactionsByAccountNumber(userName, accountNumber);
    }
}
