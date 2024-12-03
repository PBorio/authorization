package com.weblogia.authentication.model;

import com.weblogia.authentication.exceptions.UserInvalidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @ParameterizedTest
    @MethodSource("provideUserForNormalUser")
    void aNormalUserMustHaveNomePasswordCompanyAndRole(String nome, String password, Long companyId, String role) {

        Company company;
        UserRole userRole;
        if (companyId != null){
            company = new Company();
            company.setId(companyId);
        } else {
            company = null;
        }

        if (role != null) {
            userRole = new UserRole();
            userRole.setName(role);
        } else {
            userRole = null;
        }


        assertThrows(UserInvalidException.class, () -> {
            User.createUser(nome, password, company, userRole);
        });

    }

    @ParameterizedTest
    @MethodSource("provideUserForSysAdminUser")
    void aSysAdminUserMustHaveNomePasswordRole(String nome, String password, String role) {

        UserRole userRole;

        if (role != null) {
            userRole = new UserRole();
            userRole.setName(role);
        } else {
            userRole = null;
        }


        assertThrows(UserInvalidException.class, () -> {
            User.createSysAdminUser(nome, password, userRole);
        });

    }

    public static Stream<Arguments> provideUserForNormalUser() {
        return Stream.of(
                Arguments.of(null, "123456", 1l, "USER"),
                Arguments.of("Bob", null, 1l, "USER"),
                Arguments.of("Charlie", "123456", null, "USER"),
                Arguments.of("Joao", "123456", 1l, null)
        );
    }

    public static Stream<Arguments> provideUserForSysAdminUser() {
        return Stream.of(
                Arguments.of(null, "123456", "SYS_ADMIN"),
                Arguments.of("Bob", null, "SYS_ADMIN"),
                Arguments.of("Joao", "123456", null)
        );
    }

    private static User createUserWith(String role) {
        UserRole userRole = new UserRole();
        userRole.setName(role);
        User user = User.createUser("Test Name", "test-password", new Company(), userRole);
        return user;
    }

}
