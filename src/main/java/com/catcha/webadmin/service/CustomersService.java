package com.catcha.webadmin.service;

import com.catcha.webadmin.repository.CustomersRepository;
import com.catcha.webadmin.model.Customers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomersService extends AbstractService<Customers, Long> {

    @Autowired
    private CustomersRepository customersRepository;

    @Override
    protected JpaRepository<Customers, Long> getRepository() {
        return customersRepository;
    }

}
