package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.Usuario;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {
    
    private static List<Usuario> usuarios = new ArrayList<>();
    private static Long idCounter = 1L;
    
    static {
        // Datos sintéticos de prueba
        usuarios.add(new Usuario(1L, "Juan Pérez", "juan@gmail.com", "555-0001", "cliente", "juan.jpg", true));
        usuarios.add(new Usuario(2L, "María García", "maria@gmail.com", "555-0002", "proveedor", "maria.jpg", true));
        usuarios.add(new Usuario(3L, "Carlos López", "carlos@gmail.com", "555-0003", "proveedor", "carlos.jpg", true));
        usuarios.add(new Usuario(4L, "Ana Martínez", "ana@gmail.com", "555-0004", "cliente", "ana.jpg", true));
        idCounter = 5L;
    }
    
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios);
    }
    
    public Optional<Usuario> findById(Long id) {
        return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
    }
    
    public Optional<Usuario> findByEmail(String email) {
        return usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }
    
    public List<Usuario> findByRol(String rol) {
        return usuarios.stream().filter(u -> u.getRol().equals(rol)).toList();
    }
    
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(idCounter++);
        }
        usuarios.removeIf(u -> u.getId().equals(usuario.getId()));
        usuarios.add(usuario);
        return usuario;
    }
    
    public void deleteById(Long id) {
        usuarios.removeIf(u -> u.getId().equals(id));
    }
}
