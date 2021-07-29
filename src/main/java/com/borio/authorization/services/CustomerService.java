package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.CustomerForm;
import com.borio.authorization.domain.Customer;

public interface CustomerService {
    Customer save(CustomerForm customerForm);

    Customer findById(Long id);
}
