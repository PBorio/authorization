package com.borio.authorization.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private boolean enabled;

    public User(String email, String password, boolean enabled){
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }


}
