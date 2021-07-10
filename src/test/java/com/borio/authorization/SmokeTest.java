package com.borio.authorization;

import com.borio.authorization.controllers.RegisterController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private RegisterController registerController;

    @Test
    public void contextLoads() {
        assertThat(registerController).isNotNull();
    }
}
