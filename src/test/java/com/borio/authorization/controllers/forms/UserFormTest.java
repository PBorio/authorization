package com.borio.authorization.controllers.forms;

import com.borio.authorization.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserFormTest {

    @Test
    public void aNewUserMustAlwaysBeCreatedAsEnabledFalse(){
        UserForm userForm = new UserForm("test@test.com", "12345");
        User user = userForm.convertToNewUser();
        Assertions.assertFalse(user.isEnabled());
    }

}
