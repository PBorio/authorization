package com.borio.authorization.controllers;

import com.borio.authorization.controllers.dtos.CompanyDto;
import com.borio.authorization.controllers.dtos.ProposalDto;
import com.borio.authorization.controllers.dtos.ProposalServiceItemDto;
import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.controllers.forms.ProposalServiceItemForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.ProposalServiceItem;
import com.borio.authorization.services.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    private final ProposalService proposalService;

    @Autowired
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping
    public ResponseEntity<ProposalDto> create(@Valid @RequestBody ProposalForm proposalForm,
                                              UriComponentsBuilder uriComponentsBuilder) {

        Proposal createdProposal = this.proposalService.save(proposalForm);
        ProposalDto proposalDto = new ProposalDto(createdProposal);

        URI uri = uriComponentsBuilder.path("/companies/{id}").buildAndExpand(proposalDto.getId()).toUri();

        return ResponseEntity.created(uri).body(proposalDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProposalDto> getProposal(@PathVariable Long id) {

        Proposal proposal = this.proposalService.findById(id);
        ProposalDto proposalDto = new ProposalDto(proposal);

        return ResponseEntity.ok().body(proposalDto);
    }

    @PostMapping("/{proposalId}/service")
    public ResponseEntity<ProposalServiceItemDto> createService( @PathVariable Long proposalId,
                                                      @Valid @RequestBody ProposalServiceItemForm proposalServiceItemForm,
                                                      UriComponentsBuilder uriComponentsBuilder) {

        proposalServiceItemForm.setProposalId(proposalId);
        ProposalServiceItem createdProposalServiceItem = this.proposalService.saveServiceItem(proposalServiceItemForm);
        ProposalServiceItemDto proposalServiceItemDto = new ProposalServiceItemDto(createdProposalServiceItem);

        URI uri = uriComponentsBuilder.path("/proposals/{proposalId}/services/{id}")
                .buildAndExpand(proposalServiceItemDto.getProposalDto().getId(),proposalServiceItemDto.getId()).toUri();

        return ResponseEntity.created(uri).body(proposalServiceItemDto);
    }

    @DeleteMapping("/{proposalId}/service/{serviceId}")
    public ResponseEntity<ProposalServiceItemDto> createService( @PathVariable Long proposalId,
                                                                 @PathVariable Long serviceId) {

        this.proposalService.deleteServiceItem(proposalId, serviceId);
        return ResponseEntity.ok().build();
    }

}
