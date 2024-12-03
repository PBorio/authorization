package com.weblogia.authentication.model;

import com.weblogia.authentication.exceptions.PermissionInvalidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ApplicationPermissionTest {

    @ParameterizedTest
    @MethodSource("provideCompanyAndApplication")
    public void aPermissionMussHaveACompanyAndAApplication(Application application, Company company){
        if (application == null || company == null){
            Assertions.assertThrows(PermissionInvalidException.class, () -> {
                ApplicationPermission permission = new ApplicationPermission(application, company);
            });
        } else {
            Assertions.assertNotNull(new ApplicationPermission(application, company));
        }
    }

    public static Stream<Arguments> provideCompanyAndApplication() {
        return Stream.of(
                Arguments.of(new Application(), new Company()),
                Arguments.of(null, new Company()),
                Arguments.of(new Application(), null)
        );
    }
}
