package com.borio.authorization.controllers.forms;

import com.borio.authorization.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    @NotBlank(message = "{email.notempty}")
    @Email
    private String email;

    @NotBlank(message = "{pass.notempty}")
    private String password;

    public User convertToNewUser() {
        return new User(this.email, this.password, false);
    }

    public UsernamePasswordAuthenticationToken convertToAuthToken() {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
}
