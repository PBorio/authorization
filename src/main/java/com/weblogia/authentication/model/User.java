package com.weblogia.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weblogia.authentication.exceptions.UserInvalidException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    protected User() {
    }

    private User(String username, String password, Company company, UserRole role){
        this.username = username;
        this.password = password;
        this.company = company;
        this.roles.add(role);
    }

    private User(String username, String password, UserRole role){
        this.username = username;
        this.password = password;
        this.roles.add(role);
    }

    public static User createUser(String username, String password, Company company, UserRole role) {
        assertNameIsInformed(username);
        assertPasswordIsInformed(password);
        assertCompanyIsInformed(company);
        assertRoleIsInformed(role);
        return new User(username, password, company, role);
    }

    public static User createSysAdminUser(String username, String password, UserRole role) {assertNameIsInformed(username);
        assertPasswordIsInformed(password);
        assertRoleIsInformed(role);
        return new User(username, password, role);
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Set<UserRole> getRoles() {
        return this.roles;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final User other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (!Objects.equals(this$id, other$id)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (!Objects.equals(this$username, other$username)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (!Objects.equals(this$password, other$password)) return false;
        final Object this$roles = this.getRoles();
        final Object other$roles = other.getRoles();
        if (!Objects.equals(this$roles, other$roles)) return false;
        final Object this$company = this.getCompany();
        final Object other$company = other.getCompany();
        return Objects.equals(this$company, other$company);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $roles = this.getRoles();
        result = result * PRIME + ($roles == null ? 43 : $roles.hashCode());
        final Object $company = this.getCompany();
        result = result * PRIME + ($company == null ? 43 : $company.hashCode());
        return result;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", roles=" + this.getRoles() + ", company=" + this.getCompany() + ")";
    }

    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    public boolean isSysAdmin() {
        return hasRole("SYS_ADMIN");
    }

    private static void assertRoleIsInformed(UserRole role) {
        if (role == null ){
            throw new UserInvalidException("Company was not informed");
        }
    }

    private static void assertCompanyIsInformed(Company company) {
        if (company == null ){
            throw new UserInvalidException("Company was not informed");
        }
    }

    private static void assertPasswordIsInformed(String password) {
        if (password == null || "".equals(password.trim())){
            throw new UserInvalidException("Password was not informed");
        }
    }

    private static void assertNameIsInformed(String username) {
        if (username == null || "".equals(username.trim())){
            throw new UserInvalidException("User name was not informed");
        }
    }


    public void updatePassord(String newPassword) {
        this.password = newPassword;
    }
}

