package com.ServiGo.servigo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.repository.ServicioRepository;
import com.ServiGo.servigo.repository.SolicitudServicioRepository;

@Service
public class SolicitudService {

    private final SolicitudServicioRepository solicitudRepository;
    private final ServicioRepository servicioRepository;
    private final NotificacionService notificacionService;

    public SolicitudService(SolicitudServicioRepository solicitudRepository,
                           ServicioRepository servicioRepository,
                           NotificacionService notificacionService) {
        this.solicitudRepository = solicitudRepository;
        this.servicioRepository = servicioRepository;
        this.notificacionService = notificacionService;
    }

    public SolicitudServicio crear(Long servicioId, Long clienteId) {
        Servicio servicio = servicioRepository.findById(servicioId).orElse(null);
        if (servicio == null) return null;

        SolicitudServicio s = new SolicitudServicio();
        s.setClienteId(clienteId);
        s.setServicioId(servicioId);
        s.setServicioNombre(servicio.getNombre());
        s.setServicioCategoria(servicio.getCategoria());
        s.setServicioPrecio(servicio.getPrecio());
        s.setEstado("PENDIENTE");
        s.setCreadoEn(LocalDateTime.now());
        s.setActualizadoEn(LocalDateTime.now());
        SolicitudServicio guardada = solicitudRepository.save(s);

        notificacionService.crear(clienteId, guardada.getId(),
                "Solicitud enviada",
                "Tu solicitud para \"" + servicio.getNombre() + "\" fue enviada. Esperando respuesta del proveedor.",
                "mensaje");

        return guardada;
    }

    public boolean avanzar(Long id, Long clienteId) {
        SolicitudServicio s = solicitudRepository.findById(id).orElse(null);
        if (s == null || !s.getClienteId().equals(clienteId)) return false;

        LocalDateTime ahora = LocalDateTime.now();
        switch (s.getEstado()) {
            case "PENDIENTE" -> {
                s.setEstado("ACEPTADO");
                s.setAceptadoEn(ahora);
                notificacionService.crear(clienteId, s.getId(),
                        "¡Solicitud aceptada!",
                        "El proveedor aceptó tu solicitud de \"" + s.getServicioNombre() + "\". Pronto iniciará el servicio.",
                        "mensaje");
            }
            case "ACEPTADO" -> {
                s.setEstado("EN_PROGRESO");
                s.setEnProgresoEn(ahora);
                notificacionService.crear(clienteId, s.getId(),
                        "Profesional en camino",
                        "El profesional está en camino para realizar \"" + s.getServicioNombre() + "\".",
                        "alerta");
            }
            case "EN_PROGRESO" -> {
                s.setEstado("COMPLETADO");
                s.setCompletadoEn(ahora);
                notificacionService.crear(clienteId, s.getId(),
                        "¡Servicio completado!",
                        "\"" + s.getServicioNombre() + "\" fue completado exitosamente. ¡No olvides calificarlo!",
                        "oferta");
            }
            default -> { return false; }
        }

        s.setActualizadoEn(ahora);
        solicitudRepository.save(s);
        return true;
    }

    public boolean cancelar(Long id, Long clienteId) {
        SolicitudServicio s = solicitudRepository.findById(id).orElse(null);
        if (s == null || !s.getClienteId().equals(clienteId)) return false;

        if (!s.getEstado().equals("COMPLETADO")) {
            s.setEstado("CANCELADO");
            s.setActualizadoEn(LocalDateTime.now());
            solicitudRepository.save(s);
            notificacionService.crear(clienteId, s.getId(),
                    "Solicitud cancelada",
                    "Cancelaste la solicitud de \"" + s.getServicioNombre() + "\".",
                    "alerta");
        }
        return true;
    }

    public List<SolicitudServicio> obtenerPorCliente(Long clienteId) {
        return solicitudRepository.findByClienteIdOrderByCreadoEnDesc(clienteId);
    }

    public List<SolicitudServicio> filtrarActivas(List<SolicitudServicio> solicitudes) {
        return solicitudes.stream()
                .filter(s -> !s.getEstado().equals("COMPLETADO") && !s.getEstado().equals("CANCELADO"))
                .collect(Collectors.toList());
    }

    public List<SolicitudServicio> filtrarHistorial(List<SolicitudServicio> solicitudes) {
        return solicitudes.stream()
                .filter(s -> s.getEstado().equals("COMPLETADO") || s.getEstado().equals("CANCELADO"))
                .collect(Collectors.toList());
    }
}
