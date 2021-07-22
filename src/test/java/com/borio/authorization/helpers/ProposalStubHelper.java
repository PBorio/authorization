package com.borio.authorization.helpers;

import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.User;

public class ProposalStubHelper {

    public static Proposal proposalStub() {
        User user = new User();
        user.setId(1L);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");

        Company company = new Company();
        company.setId(1L);
        company.setUser(user);

        Proposal proposal = new Proposal();
        proposal.setCompany(company);
        proposal.setId(1L);
        proposal.setCustomer(customer);
        proposal.setValue(100.0);
        proposal.setDescription("A Description of the service to be done");
        proposal.setObservation("A bunch of observations about the service to be done");
        return proposal;
    }

}
