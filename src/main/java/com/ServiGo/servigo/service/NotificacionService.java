package com.ServiGo.servigo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.Notificacion;
import com.ServiGo.servigo.repository.NotificacionRepository;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> obtenerPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public int contarNoLeidas(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdAndLeidaFalse(usuarioId).size();
    }

    public void marcarTodasComoLeidas(Long usuarioId) {
        List<Notificacion> noLeidas = notificacionRepository.findByUsuarioIdAndLeidaFalse(usuarioId);
        if (!noLeidas.isEmpty()) {
            noLeidas.forEach(n -> n.setLeida(true));
            notificacionRepository.saveAll(noLeidas);
        }
    }

    public void crear(Long usuarioId, Long solicitudId, String titulo, String mensaje, String tipo) {
        Notificacion n = new Notificacion(null, titulo, mensaje, tipo, LocalDateTime.now(), false, usuarioId, solicitudId);
        notificacionRepository.save(n);
    }
}
