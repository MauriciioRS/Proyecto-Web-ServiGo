package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.SolicitudService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping("/crear")
    public String crear(@RequestParam Long servicioId, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        SolicitudServicio resultado = solicitudService.crear(servicioId, usuario.getId());
        if (resultado == null) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo crear la solicitud. Verifica que no tengas ya una solicitud activa para este servicio.");
            return "redirect:/servicios/" + servicioId;
        }
        return "redirect:/notificaciones";
    }

    @PostMapping("/{id}/avanzar")
    public String avanzar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        boolean ok = solicitudService.avanzar(id, usuario.getId());
        if (!ok) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo avanzar la solicitud. Verifica que tengas permiso y no hayas alcanzado el límite.");
            return "redirect:/notificaciones";
        }
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