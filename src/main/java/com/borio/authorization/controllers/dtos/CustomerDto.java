package com.borio.authorization.controllers.dtos;

import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long id;

    private String name;

    private String address;

    private String email;

    private String phoneNumber;

    public CustomerDto(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.address = customer.getAddress();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
    }

}
