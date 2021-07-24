package com.borio.authorization.controllers;

import com.borio.authorization.controllers.dtos.CompanyDto;
import com.borio.authorization.controllers.dtos.ProposalDto;
import com.borio.authorization.controllers.forms.CompanyForm;
import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.services.CompanyService;
import com.borio.authorization.services.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<ProposalDto> createCompany(@Valid @RequestBody ProposalForm proposalForm,
                                                    UriComponentsBuilder uriComponentsBuilder) {

        Proposal createdProposal = this.proposalService.save(proposalForm);
        ProposalDto proposalDto = new ProposalDto(createdProposal);

        URI uri = uriComponentsBuilder.path("/companies/{id}").buildAndExpand(proposalDto.getId()).toUri();

        return ResponseEntity.created(uri).body(proposalDto);
    }

}
