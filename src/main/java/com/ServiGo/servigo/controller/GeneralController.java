package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Favorito;
import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.FavoritoRepository;
import com.ServiGo.servigo.repository.ServicioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GeneralController {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

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
        List<Favorito> favs = favoritoRepository.findByUsuarioId(usuario.getId());
        List<Long> ids = favs.stream().map(Favorito::getServicioId).collect(Collectors.toList());
        List<Servicio> servicios = ids.isEmpty() ? List.of() : servicioRepository.findAllById(ids);
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

        if (favoritoRepository.existsByUsuarioIdAndServicioId(usuario.getId(), servicioId)) {
            Favorito fav = favoritoRepository
                    .findByUsuarioIdAndServicioId(usuario.getId(), servicioId).orElse(null);
            if (fav != null) favoritoRepository.delete(fav);
        } else {
            favoritoRepository.save(new Favorito(null, usuario.getId(), servicioId));
        }
        return "redirect:" + returnUrl;
    }
}
