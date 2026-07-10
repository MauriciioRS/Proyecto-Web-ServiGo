package com.ServiGo.servigo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class AuthLoginFlowTest {

    @Test
    void passwordEncoderShouldAcceptTheExpectedDemoPasswords() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "juan123";
        String encodedPassword = "$2a$10$DF/uxgFf2Dx6vFh5zZkbcujzW9L1tGGvtKGnE/FPPRL0FYSUe/MyC";

        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}
