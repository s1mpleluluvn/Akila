/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.admin.port.out;

import com.akila.entity.data.Transaction;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.List;

/**
 * @author minh
 */
public interface TransactionPort {

    //transfer money
    Transaction transferMoney(String userName, String fromAccount, String toAccount, BigDecimal amount, String description);

    List<Transaction> getTransactionsByAccountNumber(String userName,  String accountNumber, Pageable pageable);

    Long countTransactionsByAccountNumber(String userName, String accountNumber);
}
