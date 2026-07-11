package com.ServiGo.servigo.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario registrar(String nombreCompleto, String email, String telefono,
                            String dni, String fechaNacimiento, String direccion,
                            String distrito, String rol, String password) {
        Usuario usuario = new Usuario(
                null,
                nombreCompleto,
                email,
                telefono != null ? telefono.trim() : "",
                dni != null ? dni.trim() : "",
                fechaNacimiento != null ? fechaNacimiento.trim() : "",
                direccion != null ? direccion.trim() : "",
                distrito != null ? distrito.trim() : "",
                rol,
                "default.png",
                true,
                passwordEncoder.encode(password)
        );
        return usuarioRepository.save(usuario);
    }

    public void guardar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public boolean cambiarPassword(Usuario usuario, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            return false;
        }
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        return true;
    }
}
