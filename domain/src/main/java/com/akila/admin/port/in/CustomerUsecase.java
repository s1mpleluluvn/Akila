package com.akila.admin.port.in;

import com.akila.entity.data.Customer;

public interface CustomerUsecase {

    void initAdmin(String userName, String passwordHash);

    Customer findByUserName(String userName);

    Customer register(Customer user);

    //change password
    void changePassword(String userName, String passwordHash);
}
