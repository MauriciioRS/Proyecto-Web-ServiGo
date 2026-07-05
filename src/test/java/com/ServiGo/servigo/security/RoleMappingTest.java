package com.ServiGo.servigo.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.ServiGo.servigo.model.Usuario;

class RoleMappingTest {

    @Test
    void shouldMapClientAndTechnicalRolesToSpringAuthorities() {
        Usuario cliente = new Usuario();
        cliente.setRol("cliente");
        CustomUserDetails clienteDetails = new CustomUserDetails(cliente);

        Usuario tecnico = new Usuario();
        tecnico.setRol("tecnico");
        CustomUserDetails tecnicoDetails = new CustomUserDetails(tecnico);

        assertThat(clienteDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_CLIENTE");

        assertThat(tecnicoDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_TECNICO");
    }
}
