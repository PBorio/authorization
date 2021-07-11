package com.borio.authorization.controllers.forms;

import com.borio.authorization.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    private String email;

    private String password;

    public User convertToNewUser() {
        return new User(this.email, this.password, false);
    }
}
