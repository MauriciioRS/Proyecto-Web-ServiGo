package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String dni,
            @RequestParam("fecha-nacimiento") String fechaNacimiento,
            @RequestParam String direccion,
            @RequestParam String distrito,
            @RequestParam("tipo-cuenta") String tipoCuenta,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El correo ya está registrado.");
            return "redirect:/registro";
        }

        String nombreCompleto = nombres.trim() + " " + apellidos.trim();
        String rol = "contratista".equalsIgnoreCase(tipoCuenta) ? "proveedor" : "cliente";

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
                password
        );

        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Cuenta creada correctamente. Inicia sesión.");
        return "redirect:/iniciar-sesion";
    }

    @GetMapping("/iniciar-sesion")
    public String iniciarSesion() {
        return "iniciar-sesion";
    }

    @PostMapping("/iniciar-sesion")
    public String autenticarUsuario(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        var usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            session.setAttribute("usuarioLogueado", usuarioOpt.get());
            return "redirect:/";
        }

        model.addAttribute("error", "Correo o contraseña incorrectos.");
        return "iniciar-sesion";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        var usuario = (com.ServiGo.servigo.model.Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/iniciar-sesion";
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/cambiar-contrasena")
    public String cambiarContrasena() {
        return "cambiar-contrasena";
    }

    @PostMapping("/cambiar-contrasena")
    public String procesarCambiarContrasena(
            @RequestParam("current-password") String currentPassword,
            @RequestParam("new-password") String newPassword,
            @RequestParam("confirm-password") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/iniciar-sesion";
        }
        if (!usuario.getPassword().equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta.");
            return "redirect:/cambiar-contrasena";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "redirect:/cambiar-contrasena";
        }
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "La nueva contraseña debe tener al menos 6 caracteres.");
            return "redirect:/cambiar-contrasena";
        }
        usuario.setPassword(newPassword);
        usuarioRepository.save(usuario);
        session.setAttribute("usuarioLogueado", usuario);
        redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente.");
        return "redirect:/cambiar-contrasena";
    }
}
