package com.borio.authorization;

import com.borio.authorization.controllers.RegisterController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class SmokeTest {

    @Autowired
    private RegisterController registerController;

    @Test
    public void contextLoads() {
        assertThat(registerController).isNotNull();
    }
}
