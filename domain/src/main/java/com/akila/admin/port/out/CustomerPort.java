package com.akila.admin.port.out;

import com.akila.entity.data.Account;
import com.akila.entity.data.Customer;

import java.math.BigDecimal;

public interface CustomerPort {

    void initAdmin(String userName, String passwordHash);

    Customer findByUserName(String userName);

    Customer register(Customer user);

    //change password
    void changePassword(String userName, String passwordHash);

    void sendEmailAndNotification(Account fromAccount, Account toAccount, BigDecimal amount, String description);
}
