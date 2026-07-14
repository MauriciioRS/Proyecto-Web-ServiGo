package com.ServiGo.servigo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.ServicioRepository;
import com.ServiGo.servigo.repository.SolicitudServicioRepository;
import com.ServiGo.servigo.repository.UsuarioRepository;

@Service
public class SolicitudService {

    private static final int LIMITE_SOLICITUDES_ACTIVAS_CLIENTE = 3;
    private static final int LIMITE_ACEPTADOS_PROVEEDOR = 3;

    private final SolicitudServicioRepository solicitudRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public SolicitudService(SolicitudServicioRepository solicitudRepository,
                           ServicioRepository servicioRepository,
                           UsuarioRepository usuarioRepository,
                           NotificacionService notificacionService) {
        this.solicitudRepository = solicitudRepository;
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    public SolicitudServicio crear(Long servicioId, Long clienteId) {
        Servicio servicio = servicioRepository.findById(servicioId).orElse(null);
        if (servicio == null) return null;

        if (servicio.getProveedorId() != null && servicio.getProveedorId().equals(clienteId)) {
            return null;
        }

        if (solicitudRepository.existsActivaByServicioIdAndClienteId(servicioId, clienteId)) {
            return null;
        }

        Usuario cliente = usuarioRepository.findById(clienteId).orElse(null);
        if (cliente != null && !Boolean.TRUE.equals(cliente.getPremium())) {
            long activas = solicitudRepository.findByClienteIdOrderByCreadoEnDesc(clienteId).stream()
                    .filter(s -> !s.getEstado().equals("COMPLETADO") && !s.getEstado().equals("CANCELADO"))
                    .count();
            if (activas >= LIMITE_SOLICITUDES_ACTIVAS_CLIENTE) {
                return null;
            }
        }

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

        if (servicio.getProveedorId() != null) {
            notificacionService.crear(servicio.getProveedorId(), guardada.getId(),
                    "Nueva solicitud",
                    "Un cliente solicitó \"" + servicio.getNombre() + "\". Revisa los detalles.",
                    "mensaje");
        }

        return guardada;
    }

    public boolean avanzar(Long id, Long usuarioId) {
        SolicitudServicio s = solicitudRepository.findById(id).orElse(null);
        if (s == null) return false;

        boolean esProveedor = esProveedorDelServicio(s.getServicioId(), usuarioId);
        if (!esProveedor) return false;

        if ("PENDIENTE".equals(s.getEstado())) {
            Usuario proveedor = usuarioRepository.findById(usuarioId).orElse(null);
            if (proveedor != null && !Boolean.TRUE.equals(proveedor.getPremium())) {
                long aceptados = obtenerPorProveedor(usuarioId).stream()
                        .filter(sol -> "ACEPTADO".equals(sol.getEstado()))
                        .count();
                if (aceptados >= LIMITE_ACEPTADOS_PROVEEDOR) {
                    return false;
                }
            }
        }

        LocalDateTime ahora = LocalDateTime.now();
        switch (s.getEstado()) {
            case "PENDIENTE" -> {
                s.setEstado("ACEPTADO");
                s.setAceptadoEn(ahora);
                notificacionService.crear(s.getClienteId(), s.getId(),
                        "¡Solicitud aceptada!",
                        "El proveedor aceptó tu solicitud de \"" + s.getServicioNombre() + "\". Pronto iniciará el servicio.",
                        "mensaje");
            }
            case "ACEPTADO" -> {
                s.setEstado("EN_PROGRESO");
                s.setEnProgresoEn(ahora);
                notificacionService.crear(s.getClienteId(), s.getId(),
                        "Profesional en camino",
                        "El profesional está en camino para realizar \"" + s.getServicioNombre() + "\".",
                        "alerta");
            }
            case "EN_PROGRESO" -> {
                s.setEstado("COMPLETADO");
                s.setCompletadoEn(ahora);
                notificacionService.crear(s.getClienteId(), s.getId(),
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

    public boolean cancelar(Long id, Long usuarioId) {
        SolicitudServicio s = solicitudRepository.findById(id).orElse(null);
        if (s == null) return false;

        boolean esCliente = s.getClienteId().equals(usuarioId);
        boolean esProveedor = esProveedorDelServicio(s.getServicioId(), usuarioId);
        if (!esCliente && !esProveedor) return false;

        if (!s.getEstado().equals("COMPLETADO")) {
            s.setEstado("CANCELADO");
            s.setActualizadoEn(LocalDateTime.now());
            solicitudRepository.save(s);

            String por = esCliente ? "el cliente" : "el proveedor";
            if (esCliente) {
                Servicio servicio = servicioRepository.findById(s.getServicioId()).orElse(null);
                if (servicio != null && servicio.getProveedorId() != null) {
                    notificacionService.crear(servicio.getProveedorId(), s.getId(),
                            "Solicitud cancelada",
                            "El cliente canceló la solicitud de \"" + s.getServicioNombre() + "\".",
                            "alerta");
                }
            } else {
                notificacionService.crear(s.getClienteId(), s.getId(),
                        "Solicitud cancelada",
                        "El proveedor canceló la solicitud de \"" + s.getServicioNombre() + "\".",
                        "alerta");
            }
        }
        return true;
    }

    public List<SolicitudServicio> obtenerPorCliente(Long clienteId) {
        return solicitudRepository.findByClienteIdOrderByCreadoEnDesc(clienteId);
    }

    public List<SolicitudServicio> obtenerPorProveedor(Long proveedorId) {
        List<Servicio> servicios = servicioRepository.findByProveedorIdOrderByCalificacionDesc(proveedorId);
        if (servicios.isEmpty()) return List.of();
        List<Long> servicioIds = servicios.stream().map(Servicio::getId).collect(Collectors.toList());
        return solicitudRepository.findByServicioIdsOrderByCreadoEnDesc(servicioIds);
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

    private boolean esProveedorDelServicio(Long servicioId, Long usuarioId) {
        Servicio servicio = servicioRepository.findById(servicioId).orElse(null);
        return servicio != null && servicio.getProveedorId() != null && servicio.getProveedorId().equals(usuarioId);
    }
}