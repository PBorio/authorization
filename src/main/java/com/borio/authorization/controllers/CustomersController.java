package com.borio.authorization.controllers;

import com.borio.authorization.controllers.dtos.CompanyDto;
import com.borio.authorization.controllers.dtos.CustomerDto;
import com.borio.authorization.controllers.dtos.ProposalDto;
import com.borio.authorization.controllers.forms.CustomerForm;
import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/customers")
public class CustomersController {

    private CustomerService customerService;

    @Autowired
    public CustomersController(CustomerService customerService) {

        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerForm customerForm,
                                              UriComponentsBuilder uriComponentsBuilder) {

        Customer createdCustomer = this.customerService.save(customerForm);
        CustomerDto customerDto = new CustomerDto(createdCustomer);

        URI uri = uriComponentsBuilder.path("/customers/{id}").buildAndExpand(customerDto.getId()).toUri();

        return ResponseEntity.created(uri).body(customerDto);
    }



    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {

        Customer customer = this.customerService.findById(id);
        CustomerDto customerDto = new CustomerDto(customer);

        return ResponseEntity.ok().body(customerDto);
    }

}
