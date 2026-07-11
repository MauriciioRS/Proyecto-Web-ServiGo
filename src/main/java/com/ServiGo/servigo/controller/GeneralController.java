package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.Usuario;
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

    public GeneralController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
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
}
