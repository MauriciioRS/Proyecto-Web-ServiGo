package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.CalificacionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @PostMapping("/calificar")
    public String calificar(@RequestParam Long solicitudId,
                            @RequestParam Integer estrellas,
                            @RequestParam(required = false) String comentario,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        if (estrellas == null || estrellas < 1 || estrellas > 5) {
            redirectAttributes.addFlashAttribute("error", "Selecciona una calificación válida.");
            return "redirect:/notificaciones";
        }

        var calificacion = calificacionService.calificar(solicitudId, usuario.getId(), estrellas, comentario);
        if (calificacion != null) {
            redirectAttributes.addFlashAttribute("success", "¡Gracias por calificar!");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo calificar este servicio.");
        }
        return "redirect:/notificaciones";
    }
}
