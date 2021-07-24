package com.borio.authorization.domain;

import com.borio.authorization.config.security.SecurityConstants;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    //@ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    //private List<User> users;

    @Override
    public String getAuthority() {
        return SecurityConstants.SPRING_SECURITY_PREFIX+name;
    }
}
