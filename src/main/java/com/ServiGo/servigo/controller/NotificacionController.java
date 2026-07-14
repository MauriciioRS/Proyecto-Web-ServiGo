package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Notificacion;
import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.CalificacionService;
import com.ServiGo.servigo.service.NotificacionService;
import com.ServiGo.servigo.service.ServicioService;
import com.ServiGo.servigo.service.SolicitudService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final SolicitudService solicitudService;
    private final CalificacionService calificacionService;
    private final ServicioService servicioService;

    public NotificacionController(NotificacionService notificacionService,
                                  SolicitudService solicitudService,
                                  CalificacionService calificacionService,
                                  ServicioService servicioService) {
        this.notificacionService = notificacionService;
        this.solicitudService = solicitudService;
        this.calificacionService = calificacionService;
        this.servicioService = servicioService;
    }

    @GetMapping
    public String listaNotificaciones(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/iniciar-sesion";
        }

        List<Notificacion> notificaciones = notificacionService.obtenerPorUsuario(usuario.getId());
        notificacionService.marcarTodasComoLeidas(usuario.getId());

        boolean esProveedor = "proveedor".equals(usuario.getRol());

        List<SolicitudServicio> todasSolicitudes;
        if (esProveedor) {
            todasSolicitudes = solicitudService.obtenerPorProveedor(usuario.getId());
        } else {
            todasSolicitudes = solicitudService.obtenerPorCliente(usuario.getId());
        }

        List<SolicitudServicio> solicitudesActivas = solicitudService.filtrarActivas(todasSolicitudes);
        List<SolicitudServicio> solicitudesHistorial = solicitudService.filtrarHistorial(todasSolicitudes);

        Map<Long, Long> chatMap = new HashMap<>();
        if (esProveedor) {
            for (SolicitudServicio s : todasSolicitudes) {
                chatMap.put(s.getId(), s.getClienteId());
            }
        } else {
            List<Long> servicioIds = todasSolicitudes.stream()
                    .map(SolicitudServicio::getServicioId)
                    .collect(Collectors.toList());
            Map<Long, Long> servicioProveedorMap = new HashMap<>();
            if (!servicioIds.isEmpty()) {
                List<Servicio> servicios = servicioService.obtenerPorIds(servicioIds);
                for (Servicio svc : servicios) {
                    if (svc.getProveedorId() != null) {
                        servicioProveedorMap.put(svc.getId(), svc.getProveedorId());
                    }
                }
            }
            for (SolicitudServicio s : todasSolicitudes) {
                Long proveedorId = servicioProveedorMap.get(s.getServicioId());
                if (proveedorId != null) {
                    chatMap.put(s.getId(), proveedorId);
                }
            }
        }

        model.addAttribute("notificaciones", notificaciones);
        model.addAttribute("solicitudes", todasSolicitudes);
        model.addAttribute("solicitudesActivas", solicitudesActivas);
        model.addAttribute("solicitudesHistorial", solicitudesHistorial);
        model.addAttribute("esProveedor", esProveedor);
        model.addAttribute("calificacionService", calificacionService);
        model.addAttribute("chatMap", chatMap);
        return "notificaciones";
    }
}