package com.borio.authorization.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "proposal_service_item")
@Data
@NoArgsConstructor
public class ProposalServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private String observation;

    private Double value;

    @ManyToOne
    private Proposal proposal;

}
