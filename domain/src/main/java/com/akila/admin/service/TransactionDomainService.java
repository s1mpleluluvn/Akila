package com.akila.admin.service;

import com.akila.admin.port.in.TransactionUsecase;
import com.akila.admin.port.out.CustomerPort;
import com.akila.admin.port.out.TransactionPort;
import com.akila.entity.data.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TransactionDomainService implements TransactionUsecase {

    @Autowired
    public TransactionPort transactionPort;

    @Autowired
    public CustomerPort customerPort;


    @Override
    public Transaction transferMoney(String userName, String fromAccount, String toAccount, BigDecimal amount, String description) {
        var transaction = transactionPort.transferMoney(userName, fromAccount, toAccount, amount, description);
        //this for sending email and notification but using async to not block the main thread (Executor taskExecutor())
        customerPort.sendEmailAndNotification(transaction.getFromAccount(), transaction.getToAccount(), amount, description);
        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsByAccountNumber(String userName,String accountNumber, Pageable pageable) {
        return transactionPort.getTransactionsByAccountNumber(userName, accountNumber, pageable);
    }

    @Override
    public Long countTransactionsByAccountNumber(String userName, String accountNumber) {
        return transactionPort.countTransactionsByAccountNumber(userName, accountNumber);
    }
}
