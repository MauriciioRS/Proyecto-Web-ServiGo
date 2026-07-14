package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.UsuarioRepository;
import com.ServiGo.servigo.service.FavoritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GeneralController {

    private final FavoritoService favoritoService;
    private final UsuarioRepository usuarioRepository;

    public GeneralController(FavoritoService favoritoService, UsuarioRepository usuarioRepository) {
        this.favoritoService = favoritoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/landing")
    public String landing() {
        return "landing";
    }

    @GetMapping("/contactanos")
    public String contactanos() {
        return "contactanos";
    }

    @PostMapping("/contactanos")
    public String enviarContacto(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String asunto,
            @RequestParam String mensaje,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success",
                "¡Gracias " + nombre + "! Tu mensaje fue recibido. Te responderemos a " + email + " en menos de 24 horas.");
        return "redirect:/contactanos";
    }

    @GetMapping("/quienes-somos")
    public String quienesSomos() {
        return "quienes-somos";
    }

    @GetMapping("/favoritos")
    public String favoritos(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/iniciar-sesion";
        }
        List<Servicio> servicios = favoritoService.obtenerFavoritos(usuario.getId());
        model.addAttribute("serviciosFavoritos", servicios);
        return "favoritos";
    }

    @PostMapping("/favoritos/toggle")
    public String toggleFavorito(
            @RequestParam Long servicioId,
            @RequestParam(defaultValue = "/favoritos") String returnUrl,
            HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        favoritoService.toggle(usuario.getId(), servicioId);
        return "redirect:" + returnUrl;
    }

    @GetMapping("/premium")
    public String premium(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            model.addAttribute("yaPremium", Boolean.TRUE.equals(usuario.getPremium()));
        } else {
            model.addAttribute("yaPremium", false);
        }
        return "premium";
    }

    @PostMapping("/premium/activar")
    public String activarPremium(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        usuario.setPremium(true);
        usuarioRepository.save(usuario);
        session.setAttribute("usuarioLogueado", usuario);
        redirectAttributes.addFlashAttribute("success", "¡Felicitaciones! Ahora eres Premium. Disfruta de beneficios ilimitados.");
        return "redirect:/";
    }

    @PostMapping("/premium/desactivar")
    public String desactivarPremium(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        usuario.setPremium(false);
        usuarioRepository.save(usuario);
        session.setAttribute("usuarioLogueado", usuario);
        redirectAttributes.addFlashAttribute("success", "Has desactivado tu membresía Premium.");
        return "redirect:/";
    }
}