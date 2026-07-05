package com.ServiGo.servigo;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void securityBeansAreRegistered() {
        assertThat(applicationContext.getBean(PasswordEncoder.class)).isNotNull();
        assertThat(applicationContext.getBean(UserDetailsService.class)).isNotNull();
    }
}
