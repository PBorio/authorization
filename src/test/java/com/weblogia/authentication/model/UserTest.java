package com.weblogia.authentication.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

public class UserTest {

    @ParameterizedTest
    @ValueSource(strings = {"SYS_ADMIN", "USER"})
    public void aUserShouldKnowIfItHasARole(String role){

        User user = createUserWith(role);

        Assertions.assertTrue(user.hasRole(role), "User has not the expected role");
        Assertions.assertFalse(user.hasRole("ANOTHER_ROLE"), "User has the unexpected role");
    }

    @ParameterizedTest
    @ValueSource(strings = {"SYS_ADMIN", "ADMIN"})
    public void aUserShouldKnowHeIsAnSysAdmin(String role) {
        User user = createUserWith(role);
        Assertions.assertEquals("SYS_ADMIN".equals(role), user.isSysAdmin(), "Unexpected result for isSysAdmin");
    }

    private static User createUserWith(String role) {
        User user = new User();
        UserRole userRole = new UserRole();
        userRole.setName(role);
        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        return user;
    }

}
