package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.SolicitudService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping("/crear")
    public String crear(@RequestParam Long servicioId, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        solicitudService.crear(servicioId, usuario.getId());
        return "redirect:/notificaciones";
    }

    @PostMapping("/{id}/avanzar")
    public String avanzar(@PathVariable Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        solicitudService.avanzar(id, usuario.getId());
        return "redirect:/notificaciones";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        solicitudService.cancelar(id, usuario.getId());
        return "redirect:/notificaciones";
    }
}
