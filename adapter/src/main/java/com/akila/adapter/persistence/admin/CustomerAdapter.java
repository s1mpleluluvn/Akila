package com.akila.adapter.persistence.admin;

import com.akila.adapter.persistence.restService.EmailService;
import com.akila.entity.data.Account;
import com.akila.entity.data.Customer;
import com.akila.service.CustomerService;
import com.akila.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.akila.admin.port.out.CustomerPort;

import java.math.BigDecimal;

@Component
public class CustomerAdapter implements CustomerPort {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void initAdmin(String userName, String passwordHash) {
        customerService.initAdmin(userName, passwordHash);
    }

    //find by username
    @Override
    public Customer findByUserName(String userName) {
        return customerService.findByUserName(userName);
    }

    @Override
    public Customer register(Customer user) {
        return customerService.register(user);
    }

    //change password
    @Override
    public void changePassword(String userName, String passwordHash) {
        customerService.changePassword(userName, passwordHash);
    }

    @Override
    public void sendEmailAndNotification(Account fromAccount, Account toAccount, BigDecimal amount, String description) {
        //send email to smtp server
        try {
            emailService.sendMessage("akila@gmail.com", toAccount.getCustomer().getEmail(), "Transaction Notification", "You have transferred " + amount + " to " + toAccount.getAccountNumber());
        } catch (Exception e) {
        }
        //save email to database
        notificationService.sendEmail(toAccount.getCustomer().getEmail(), "You have received " + amount + " from " + fromAccount.getAccountNumber());
        //send notification
        notificationService.sendNotification(toAccount.getCustomer().getEmail(), "You have received " + amount + " from " + fromAccount.getAccountNumber());
    }
}
