package com.borio.authorization.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "proposals")
@Data
@NoArgsConstructor
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private String observation;

    private Double value;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Company company;

}
