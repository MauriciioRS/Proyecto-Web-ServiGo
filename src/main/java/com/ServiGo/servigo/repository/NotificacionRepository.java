package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.Notificacion;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class NotificacionRepository {
    
    private static List<Notificacion> notificaciones = new ArrayList<>();
    private static Long idCounter = 1L;
    
    static {
        // Datos sintéticos de prueba
        notificaciones.add(new Notificacion(1L, "Nueva Oferta", "Descuento 20% en servicios de limpieza", "oferta", LocalDateTime.now().minusHours(2), false, 1L));
        notificaciones.add(new Notificacion(2L, "Servicio Completado", "Tu servicio de plomería ha sido completado", "alerta", LocalDateTime.now().minusHours(5), true, 1L));
        notificaciones.add(new Notificacion(3L, "Nuevo Mensaje", "María García te envió un mensaje", "mensaje", LocalDateTime.now().minusMinutes(30), false, 1L));
        notificaciones.add(new Notificacion(4L, "Recordatorio", "No olvides calificar el servicio", "alerta", LocalDateTime.now().minusHours(1), false, 1L));
        idCounter = 5L;
    }
    
    public List<Notificacion> findAll() {
        return new ArrayList<>(notificaciones);
    }
    
    public Optional<Notificacion> findById(Long id) {
        return notificaciones.stream().filter(n -> n.getId().equals(id)).findFirst();
    }
    
    public List<Notificacion> findByUsuarioId(Long usuarioId) {
        return notificaciones.stream().filter(n -> n.getUsuarioId().equals(usuarioId)).toList();
    }
    
    public List<Notificacion> findByUsuarioIdAndLeidaFalse(Long usuarioId) {
        return notificaciones.stream()
            .filter(n -> n.getUsuarioId().equals(usuarioId) && !n.getLeida())
            .toList();
    }
    
    public Notificacion save(Notificacion notificacion) {
        if (notificacion.getId() == null) {
            notificacion.setId(idCounter++);
        }
        notificaciones.removeIf(n -> n.getId().equals(notificacion.getId()));
        notificaciones.add(notificacion);
        return notificacion;
    }
    
    public void deleteById(Long id) {
        notificaciones.removeIf(n -> n.getId().equals(id));
    }
}
