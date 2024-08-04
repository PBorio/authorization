package com.weblogia.authentication.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@SuppressWarnings("unused")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ApplicationPermission> applicationPermissions;

    public Company() {
    }


    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public Set<ApplicationPermission> getApplicationPermissions() {
        return this.applicationPermissions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setApplicationPermissions(Set<ApplicationPermission> applicationPermissions) {
        this.applicationPermissions = applicationPermissions;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Company other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (!Objects.equals(this$id, other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (!Objects.equals(this$name, other$name)) return false;
        final Object this$users = this.getUsers();
        final Object other$users = other.getUsers();
        if (!Objects.equals(this$users, other$users)) return false;
        final Object this$applicationPermissions = this.getApplicationPermissions();
        final Object other$applicationPermissions = other.getApplicationPermissions();
        return Objects.equals(this$applicationPermissions, other$applicationPermissions);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Company;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $users = this.getUsers();
        result = result * PRIME + ($users == null ? 43 : $users.hashCode());
        final Object $applicationPermissions = this.getApplicationPermissions();
        result = result * PRIME + ($applicationPermissions == null ? 43 : $applicationPermissions.hashCode());
        return result;
    }

    public String toString() {
        return "Company(id=" + this.getId() + ", name=" + this.getName() + ", users=" + this.getUsers() + ", applicationPermissions=" + this.getApplicationPermissions() + ")";
    }
}
