package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Notificacion;
import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.NotificacionRepository;
import com.ServiGo.servigo.repository.ServicioRepository;
import com.ServiGo.servigo.repository.SolicitudServicioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {

    @Autowired private SolicitudServicioRepository solicitudRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private NotificacionRepository notificacionRepository;

    @PostMapping("/crear")
    public String crear(@RequestParam Long servicioId, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        Servicio servicio = servicioRepository.findById(servicioId).orElse(null);
        if (servicio == null) return "redirect:/servicios";

        SolicitudServicio s = new SolicitudServicio();
        s.setClienteId(usuario.getId());
        s.setServicioId(servicioId);
        s.setServicioNombre(servicio.getNombre());
        s.setServicioCategoria(servicio.getCategoria());
        s.setServicioPrecio(servicio.getPrecio());
        s.setEstado("PENDIENTE");
        s.setCreadoEn(LocalDateTime.now());
        s.setActualizadoEn(LocalDateTime.now());
        SolicitudServicio guardada = solicitudRepository.save(s);

        crearNotificacion(usuario.getId(), guardada.getId(),
                "Solicitud enviada",
                "Tu solicitud para \"" + servicio.getNombre() + "\" fue enviada. Esperando respuesta del proveedor.",
                "mensaje");

        return "redirect:/notificaciones";
    }

    @PostMapping("/{id}/avanzar")
    public String avanzar(@PathVariable Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        SolicitudServicio s = solicitudRepository.findById(id).orElse(null);
        if (s == null || !s.getClienteId().equals(usuario.getId())) return "redirect:/notificaciones";

        LocalDateTime ahora = LocalDateTime.now();
        switch (s.getEstado()) {
            case "PENDIENTE" -> {
                s.setEstado("ACEPTADO");
                s.setAceptadoEn(ahora);
                crearNotificacion(usuario.getId(), s.getId(),
                        "¡Solicitud aceptada!",
                        "El proveedor aceptó tu solicitud de \"" + s.getServicioNombre() + "\". Pronto iniciará el servicio.",
                        "mensaje");
            }
            case "ACEPTADO" -> {
                s.setEstado("EN_PROGRESO");
                s.setEnProgresoEn(ahora);
                crearNotificacion(usuario.getId(), s.getId(),
                        "Profesional en camino",
                        "El profesional está en camino para realizar \"" + s.getServicioNombre() + "\".",
                        "alerta");
            }
            case "EN_PROGRESO" -> {
                s.setEstado("COMPLETADO");
                s.setCompletadoEn(ahora);
                crearNotificacion(usuario.getId(), s.getId(),
                        "¡Servicio completado!",
                        "\"" + s.getServicioNombre() + "\" fue completado exitosamente. ¡No olvides calificarlo!",
                        "oferta");
            }
            default -> { return "redirect:/notificaciones"; }
        }

        s.setActualizadoEn(ahora);
        solicitudRepository.save(s);
        return "redirect:/notificaciones";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        SolicitudServicio s = solicitudRepository.findById(id).orElse(null);
        if (s == null || !s.getClienteId().equals(usuario.getId())) return "redirect:/notificaciones";

        if (!s.getEstado().equals("COMPLETADO")) {
            s.setEstado("CANCELADO");
            s.setActualizadoEn(LocalDateTime.now());
            solicitudRepository.save(s);
            crearNotificacion(usuario.getId(), s.getId(),
                    "Solicitud cancelada",
                    "Cancelaste la solicitud de \"" + s.getServicioNombre() + "\".",
                    "alerta");
        }
        return "redirect:/notificaciones";
    }

    private void crearNotificacion(Long usuarioId, Long solicitudId, String titulo, String mensaje, String tipo) {
        Notificacion n = new Notificacion(null, titulo, mensaje, tipo, LocalDateTime.now(), false, usuarioId, solicitudId);
        notificacionRepository.save(n);
    }
}
