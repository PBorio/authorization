package com.borio.authorization.controllers.dtos;

import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.ProposalServiceItem;
import com.borio.authorization.services.ProposalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalServiceItemDto {

    private Long id;

    private String description;

    private String observation;

    private Double value;

    private ProposalDto proposalDto;

    public ProposalServiceItemDto(ProposalServiceItem proposalService) {
        this.id = proposalService.getId();
        this.description = proposalService.getDescription();
        this.observation = proposalService.getObservation();
        this.value = proposalService.getValue();
        this.proposalDto = new ProposalDto();
        this.proposalDto.setId(proposalService.getProposal().getId());
        this.proposalDto.setDescription(proposalService.getDescription());
        this.proposalDto.setObservation(proposalService.getObservation());
        this.proposalDto.setValue(proposalService.getValue());
        this.proposalDto.setCustomer(new CustomerDto(proposalService.getProposal().getCustomer()));
    }

}
