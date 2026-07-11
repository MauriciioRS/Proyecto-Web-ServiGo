package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Notificacion;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.NotificacionService;
import com.ServiGo.servigo.service.SolicitudService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final SolicitudService solicitudService;

    public NotificacionController(NotificacionService notificacionService, SolicitudService solicitudService) {
        this.notificacionService = notificacionService;
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listaNotificaciones(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/iniciar-sesion";
        }

        List<Notificacion> notificaciones = notificacionService.obtenerPorUsuario(usuario.getId());
        notificacionService.marcarTodasComoLeidas(usuario.getId());

        List<SolicitudServicio> todasSolicitudes = solicitudService.obtenerPorCliente(usuario.getId());

        List<SolicitudServicio> solicitudesActivas = solicitudService.filtrarActivas(todasSolicitudes);
        List<SolicitudServicio> solicitudesHistorial = solicitudService.filtrarHistorial(todasSolicitudes);

        model.addAttribute("notificaciones", notificaciones);
        model.addAttribute("solicitudes", todasSolicitudes);
        model.addAttribute("solicitudesActivas", solicitudesActivas);
        model.addAttribute("solicitudesHistorial", solicitudesHistorial);
        return "notificaciones";
    }
}
