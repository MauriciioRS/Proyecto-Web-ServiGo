package com.ServiGo.servigo.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.ContactoConversacion;
import com.ServiGo.servigo.model.MensajeChat;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.MensajeChatRepository;
import com.ServiGo.servigo.repository.UsuarioRepository;

@Service
public class MensajeService {

    private final MensajeChatRepository mensajeChatRepository;
    private final UsuarioRepository usuarioRepository;

    public MensajeService(MensajeChatRepository mensajeChatRepository, UsuarioRepository usuarioRepository) {
        this.mensajeChatRepository = mensajeChatRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public long calcularConversacionId(long userId1, long userId2) {
        return Math.min(userId1, userId2) * 10000L + Math.max(userId1, userId2);
    }

    public void enviar(Long senderId, Long otroUsuarioId, String texto) {
        String limpio = texto.trim();
        if (limpio.length() > 2000) limpio = limpio.substring(0, 2000);

        long cid = calcularConversacionId(senderId, otroUsuarioId);
        MensajeChat m = new MensajeChat(null, cid, senderId, false, limpio, LocalDateTime.now());
        mensajeChatRepository.save(m);
    }

    public List<MensajeChat> obtenerMensajes(Long userId1, Long userId2) {
        long cid = calcularConversacionId(userId1, userId2);
        return mensajeChatRepository.findByConversacionIdOrderByCreadoEnAsc(cid);
    }

    public List<Map<String, Object>> obtenerNuevos(Long userId1, Long otroUsuarioId, Long lastId) {
        long cid = calcularConversacionId(userId1, otroUsuarioId);
        List<MensajeChat> nuevos = mensajeChatRepository
                .findByConversacionIdAndIdGreaterThanOrderByCreadoEnAsc(cid, lastId);

        return nuevos.stream().map(msg -> {
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("id", msg.getId());
            r.put("texto", msg.getTexto());
            r.put("hora", msg.getHoraCorta());
            r.put("mio", Objects.equals(msg.getSenderUserId(), userId1));
            return r;
        }).collect(Collectors.toList());
    }

    public List<ContactoConversacion> buildContactos(Usuario yo) {
        return usuarioRepository.findAll().stream()
                .filter(u -> !u.getId().equals(yo.getId()))
                .map(u -> {
                    String avatar = "https://ui-avatars.com/api/?name="
                            + u.getNombre().replace(" ", "+")
                            + "&background=0d6efd&color=fff&size=128";
                    ContactoConversacion c = new ContactoConversacion(
                            u.getId(), u.getNombre(), u.getRol(), avatar, "", "");
                    long cid = calcularConversacionId(yo.getId(), u.getId());
                    mensajeChatRepository.findTopByConversacionIdOrderByCreadoEnDesc(cid)
                            .ifPresent(m -> {
                                c.setUltimaVistaPrevia(vistaPrevia(m.getTexto()));
                                c.setUltimoTiempoRelativo(tiempoRelativo(m.getCreadoEn()));
                            });
                    return c;
                })
                .collect(Collectors.toList());
    }

    private static String vistaPrevia(String texto) {
        if (texto == null) return "";
        String t = texto.replaceAll("\\s+", " ").trim();
        return t.length() <= 56 ? t : t.substring(0, 53) + "…";
    }

    private static String tiempoRelativo(LocalDateTime cuando) {
        if (cuando == null) return "";
        Duration d = Duration.between(cuando, LocalDateTime.now());
        if (d.isNegative()) return "Ahora";
        long mins = d.toMinutes();
        if (mins < 1) return "Ahora";
        if (mins < 60) return mins + "m";
        long hours = d.toHours();
        if (hours < 24) return hours + "h";
        long days = d.toDays();
        if (days < 7) return days + "d";
        return cuando.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM"));
    }
}
