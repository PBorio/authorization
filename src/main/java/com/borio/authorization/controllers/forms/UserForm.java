package com.borio.authorization.controllers.forms;

import com.borio.authorization.domain.User;
import lombok.Data;

@Data
public class UserForm {

    private String email;

    private String password;

    public User convert() {
        return new User(this.email, this.password);
    }
}
