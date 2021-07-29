package com.borio.authorization.controllers.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalServiceItemForm {

    private Long id;

    @NotBlank(message = "{proposal.service.descpription.notempty}")
    private String description;

    private String observation;

    private Double value;

    private Long proposalId;
}
