package com.borio.authorization.controllers.dtos;

import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Proposal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDto {


    private Long id;

    private String description;

    private String observation;

    private Double value;

    private CustomerDto customer;

    private CompanyDto company;

    public ProposalDto(Proposal proposal) {
        this.id = proposal.getId();
        this.description = proposal.getDescription();
        this.observation = proposal.getObservation();
        this.value = proposal.getValue();
        this.customer = new CustomerDto(proposal.getCustomer());
        this.company = new CompanyDto(proposal.getCompany());
    }
}
