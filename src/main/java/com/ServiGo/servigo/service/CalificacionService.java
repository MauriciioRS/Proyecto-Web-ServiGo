package com.ServiGo.servigo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.Calificacion;
import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.repository.CalificacionRepository;
import com.ServiGo.servigo.repository.ServicioRepository;
import com.ServiGo.servigo.repository.SolicitudServicioRepository;

@Service
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final ServicioRepository servicioRepository;
    private final SolicitudServicioRepository solicitudRepository;

    public CalificacionService(CalificacionRepository calificacionRepository,
                               ServicioRepository servicioRepository,
                               SolicitudServicioRepository solicitudRepository) {
        this.calificacionRepository = calificacionRepository;
        this.servicioRepository = servicioRepository;
        this.solicitudRepository = solicitudRepository;
    }

    public Calificacion calificar(Long solicitudId, Long clienteId, Integer estrellas, String comentario) {
        if (calificacionRepository.existsBySolicitudId(solicitudId)) return null;

        SolicitudServicio solicitud = solicitudRepository.findById(solicitudId).orElse(null);
        if (solicitud == null || !solicitud.getClienteId().equals(clienteId)) return null;
        if (!"COMPLETADO".equals(solicitud.getEstado())) return null;

        Calificacion c = new Calificacion();
        c.setSolicitudId(solicitudId);
        c.setClienteId(clienteId);
        c.setServicioId(solicitud.getServicioId());
        c.setEstrellas(estrellas);
        c.setComentario(comentario);
        c.setCreadoEn(LocalDateTime.now());

        Servicio servicio = servicioRepository.findById(solicitud.getServicioId()).orElse(null);
        if (servicio != null) {
            c.setProveedorId(servicio.getProveedorId());
        }

        Calificacion guardada = calificacionRepository.save(c);
        actualizarPromedioServicio(solicitud.getServicioId());
        return guardada;
    }

    public Optional<Calificacion> obtenerPorSolicitud(Long solicitudId) {
        return calificacionRepository.findBySolicitudId(solicitudId);
    }

    public boolean yaCalificada(Long solicitudId) {
        return calificacionRepository.existsBySolicitudId(solicitudId);
    }

    public List<Calificacion> obtenerPorProveedor(Long proveedorId) {
        return calificacionRepository.findByProveedorIdOrderByCreadoEnDesc(proveedorId);
    }

    public List<Calificacion> obtenerPorCliente(Long clienteId) {
        return calificacionRepository.findByClienteIdOrderByCreadoEnDesc(clienteId);
    }

    public double promedioProveedor(Long proveedorId) {
        List<Calificacion> calificaciones = calificacionRepository.findByProveedorIdOrderByCreadoEnDesc(proveedorId);
        if (calificaciones.isEmpty()) return 0.0;
        return calificaciones.stream()
                .mapToInt(Calificacion::getEstrellas)
                .average()
                .orElse(0.0);
    }

    private void actualizarPromedioServicio(Long servicioId) {
        Servicio servicio = servicioRepository.findById(servicioId).orElse(null);
        if (servicio == null) return;

        List<Calificacion> todas = calificacionRepository.findByProveedorIdOrderByCreadoEnDesc(servicio.getProveedorId());
        long delServicio = todas.stream()
                .filter(c -> c.getServicioId().equals(servicioId))
                .count();
        if (delServicio > 0) {
            int promedio = (int) todas.stream()
                    .filter(c -> c.getServicioId().equals(servicioId))
                    .mapToInt(Calificacion::getEstrellas)
                    .average()
                    .orElse(0);
            servicio.setCalificacion(promedio);
            servicioRepository.save(servicio);
        }
    }
}
