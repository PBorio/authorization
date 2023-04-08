package com.borio.authorization.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "companies")
@NoArgsConstructor
public class Logo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Company company;

    private byte[] logo;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company){
        this.company = company;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
