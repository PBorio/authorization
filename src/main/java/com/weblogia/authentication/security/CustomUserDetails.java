package com.weblogia.authentication.security;

import com.weblogia.authentication.model.User;
import com.weblogia.authentication.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends User implements UserDetails {

    private final String username;
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User byUsername) {
        this.username = byUsername.getUsername();
        this.password= byUsername.getPassword();
        super.setCompany(byUsername.getCompany());
        List<GrantedAuthority> auths = new ArrayList<>();

        for(UserRole role : byUsername.getRoles()){
            auths.add(new SimpleGrantedAuthority("ROLE_"+role.getName().toUpperCase()));
        }
        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
