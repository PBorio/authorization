package com.weblogia.authentication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.weblogia.authentication.exceptions.PermissionInvalidException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "application_permission")
@SuppressWarnings("unused")
public class ApplicationPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;

    private LocalDate startDate;
    private LocalDate endDate;

    protected ApplicationPermission() {}

    public ApplicationPermission(Application application, Company company) {

        applicationCannotBeNull(application);
        companyCannotBeNull(company);

        this.application = application;
        this.company = company;
        this.startDate = LocalDate.now();
    }

    public void revoke() {
        this.endDate = LocalDate.now();
    }

    private void companyCannotBeNull(Company company) {
        if (company == null)
            throw new PermissionInvalidException("A company must be informed to a Permission to be valid");
    }

    private void applicationCannotBeNull(Application application) {
        if (application == null)
            throw new PermissionInvalidException("An Application must be informed for a permission to be valid.");
    }


    public Long getId() {
        return this.id;
    }

    public Application getApplication() {
        return this.application;
    }

    public Company getCompany() {
        return this.company;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final ApplicationPermission other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (!Objects.equals(this$id, other$id)) return false;
        final Object this$application = this.getApplication();
        final Object other$application = other.getApplication();
        if (!Objects.equals(this$application, other$application)) return false;
        final Object this$company = this.getCompany();
        final Object other$company = other.getCompany();
        if (!Objects.equals(this$company, other$company)) return false;
        final Object this$startDate = this.getStartDate();
        final Object other$startDate = other.getStartDate();
        if (!Objects.equals(this$startDate, other$startDate)) return false;
        final Object this$endDate = this.getEndDate();
        final Object other$endDate = other.getEndDate();
        return Objects.equals(this$endDate, other$endDate);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ApplicationPermission;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $application = this.getApplication();
        result = result * PRIME + ($application == null ? 43 : $application.hashCode());
        final Object $company = this.getCompany();
        result = result * PRIME + ($company == null ? 43 : $company.hashCode());
        final Object $startDate = this.getStartDate();
        result = result * PRIME + ($startDate == null ? 43 : $startDate.hashCode());
        final Object $endDate = this.getEndDate();
        result = result * PRIME + ($endDate == null ? 43 : $endDate.hashCode());
        return result;
    }

    public String toString() {
        return "ApplicationPermission(id=" + this.getId() + ", application=" + this.getApplication() + ", company=" + this.getCompany() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ")";
    }


}
