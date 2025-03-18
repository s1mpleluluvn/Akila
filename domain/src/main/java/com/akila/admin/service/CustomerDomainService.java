package com.akila.admin.service;

import com.akila.admin.port.in.CustomerUsecase;
import com.akila.admin.port.out.CustomerPort;
import com.akila.entity.data.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerDomainService implements CustomerUsecase {

    @Autowired
    private CustomerPort customerPort;

    @Override
    public void initAdmin(String userName, String passwordHash) {
        customerPort.initAdmin(userName, passwordHash);

    }

    @Override
    public Customer findByUserName(String userName) {
        return customerPort.findByUserName(userName);
    }

    @Override
    public Customer register(Customer user) {
        return customerPort.register(user);
    }

    @Override
    public void changePassword(String userName, String passwordHash) {
        customerPort.changePassword(userName, passwordHash);
    }
}
