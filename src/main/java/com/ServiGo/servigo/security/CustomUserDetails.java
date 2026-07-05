package com.ServiGo.servigo.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ServiGo.servigo.model.Usuario;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = normalizeRole(usuario.getRol());
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    private String normalizeRole(String rol) {
        if (rol == null) {
            return "CLIENTE";
        }

        String normalized = rol.trim().toLowerCase();
        if ("tecnico".equals(normalized) || "profesional".equals(normalized) || "contratista".equals(normalized) || "provider".equals(normalized)) {
            return "TECNICO";
        }
        return "CLIENTE";
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(usuario.getActivo());
    }
}
