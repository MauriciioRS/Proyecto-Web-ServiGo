package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Notificacion;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.NotificacionRepository;
import com.ServiGo.servigo.repository.SolicitudServicioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private SolicitudServicioRepository solicitudRepository;

    @GetMapping
    public String listaNotificaciones(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/iniciar-sesion";
        }

        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioId(usuario.getId());

        // Mark all unread notifications as read
        List<Notificacion> noLeidas = notificaciones.stream()
                .filter(n -> !Boolean.TRUE.equals(n.getLeida()))
                .collect(Collectors.toList());
        if (!noLeidas.isEmpty()) {
            noLeidas.forEach(n -> n.setLeida(true));
            notificacionRepository.saveAll(noLeidas);
        }

        List<SolicitudServicio> todasSolicitudes =
                solicitudRepository.findByClienteIdOrderByCreadoEnDesc(usuario.getId());

        List<SolicitudServicio> solicitudesActivas = todasSolicitudes.stream()
                .filter(s -> !s.getEstado().equals("COMPLETADO") && !s.getEstado().equals("CANCELADO"))
                .collect(Collectors.toList());

        List<SolicitudServicio> solicitudesHistorial = todasSolicitudes.stream()
                .filter(s -> s.getEstado().equals("COMPLETADO") || s.getEstado().equals("CANCELADO"))
                .collect(Collectors.toList());

        model.addAttribute("notificaciones", notificaciones);
        model.addAttribute("solicitudes", todasSolicitudes);
        model.addAttribute("solicitudesActivas", solicitudesActivas);
        model.addAttribute("solicitudesHistorial", solicitudesHistorial);
        return "notificaciones";
    }
}
