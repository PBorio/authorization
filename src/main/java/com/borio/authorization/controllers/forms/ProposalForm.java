package com.borio.authorization.controllers.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalForm {

    private Long id;

    @NotBlank(message = "{proposal.descpription.notempty}")
    private String description;

    private String observation;

    private Double value;

    private Long customerId;

    private String customerName;

    @NotNull(message = "{proposal.company.notempty}")
    private Long companyId;

}
