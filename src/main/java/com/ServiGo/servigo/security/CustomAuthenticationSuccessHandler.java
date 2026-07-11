package com.ServiGo.servigo.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.UsuarioRepository;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;

    public CustomAuthenticationSuccessHandler(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario != null) {
            request.getSession().setAttribute("usuarioLogueado", usuario);
        }
        response.sendRedirect("/");
    }
}
