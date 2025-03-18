package com.akila.admin.port.in;

import com.akila.entity.data.Transaction;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionUsecase {

    //transfer money
    Transaction transferMoney(String userName, String fromAccount, String toAccount, BigDecimal amount, String description);

    List<Transaction> getTransactionsByAccountNumber(String accountNumber, Pageable pageable);
}
