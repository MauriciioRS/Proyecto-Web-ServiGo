package com.ServiGo.servigo.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.ContactoConversacion;
import com.ServiGo.servigo.model.MensajeChat;
import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.MensajeChatRepository;
import com.ServiGo.servigo.repository.ServicioRepository;
import com.ServiGo.servigo.repository.SolicitudServicioRepository;
import com.ServiGo.servigo.repository.UsuarioRepository;

@Service
public class MensajeService {

    private final MensajeChatRepository mensajeChatRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudServicioRepository solicitudRepository;
    private final ServicioRepository servicioRepository;

    public MensajeService(MensajeChatRepository mensajeChatRepository,
                          UsuarioRepository usuarioRepository,
                          SolicitudServicioRepository solicitudRepository,
                          ServicioRepository servicioRepository) {
        this.mensajeChatRepository = mensajeChatRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudRepository = solicitudRepository;
        this.servicioRepository = servicioRepository;
    }

    public long calcularConversacionId(long userId1, long userId2) {
        return Math.min(userId1, userId2) * 10000L + Math.max(userId1, userId2);
    }

    public void enviar(Long senderId, Long otroUsuarioId, String texto) {
        String limpio = texto.trim();
        if (limpio.length() > 2000) limpio = limpio.substring(0, 2000);

        Usuario sender = usuarioRepository.findById(senderId).orElse(null);
        boolean esProveedor = sender != null && "proveedor".equals(sender.getRol());

        long cid = calcularConversacionId(senderId, otroUsuarioId);
        MensajeChat m = new MensajeChat(null, cid, senderId, esProveedor, limpio, LocalDateTime.now(), false);
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
        boolean esProveedor = "proveedor".equals(yo.getRol());

        Set<Long> contactoIds = new HashSet<>();

        // 1. Contactos derivados de solicitudes (batch)
        if (esProveedor) {
            List<Servicio> misServicios = servicioRepository.findByProveedorIdOrderByCalificacionDesc(yo.getId());
            if (!misServicios.isEmpty()) {
                List<Long> servicioIds = misServicios.stream().map(Servicio::getId).collect(Collectors.toList());
                List<SolicitudServicio> solicitudes = solicitudRepository.findByServicioIdsOrderByCreadoEnDesc(servicioIds);
                solicitudes.forEach(s -> contactoIds.add(s.getClienteId()));
            }
        } else {
            List<SolicitudServicio> solicitudes = solicitudRepository.findByClienteIdOrderByCreadoEnDesc(yo.getId());
            if (!solicitudes.isEmpty()) {
                List<Long> servicioIds = solicitudes.stream().map(SolicitudServicio::getServicioId).distinct().collect(Collectors.toList());
                List<Long> proveedorIds = servicioRepository.findProveedorIdsByServicioIds(servicioIds);
                contactoIds.addAll(proveedorIds);
            }
        }

        // 2. Contactos de conversaciones existentes (batch — N+1 fixed)
        List<Long> allCids = mensajeChatRepository.findAllDistinctConversacionIds();
        List<Long> conversacionIds = allCids.stream()
                .filter(cid -> {
                    long a = cid / 10000L;
                    long b = cid % 10000L;
                    return a == yo.getId() || b == yo.getId();
                })
                .collect(Collectors.toList());
        if (!conversacionIds.isEmpty()) {
            for (Long cid : conversacionIds) {
                long otherUserId = extraerOtroUserId(cid, yo.getId());
                if (otherUserId > 0) {
                    contactoIds.add(otherUserId);
                }
            }
        }

        if (contactoIds.isEmpty()) return List.of();

        // 3. Batch load all users at once (1 query instead of N)
        List<Long> idsList = new ArrayList<>(contactoIds);
        Map<Long, Usuario> usuariosMap = usuarioRepository.findAllById(idsList).stream()
                .collect(Collectors.toMap(Usuario::getId, u -> u));

        // 4. Batch load last messages and unread counts
        Map<Long, Long> cidToUserId = new HashMap<>();
        for (Long contactId : contactoIds) {
            long cid = calcularConversacionId(yo.getId(), contactId);
            cidToUserId.put(cid, contactId);
        }

        List<Long> myCids = new ArrayList<>(cidToUserId.keySet());

        Map<Long, MensajeChat> lastMessageMap = new HashMap<>();
        if (!myCids.isEmpty()) {
            List<MensajeChat> lastMessages = mensajeChatRepository.findLastMessagesByConversacionIds(myCids);
            lastMessages.forEach(m -> lastMessageMap.put(m.getConversacionId(), m));
        }

        Map<Long, Long> unreadMap = new HashMap<>();
        if (!myCids.isEmpty()) {
            List<MensajeChat> unreadMessages = mensajeChatRepository.countUnreadByConversacionIds(myCids, yo.getId());
            Map<Long, Long> tempMap = unreadMessages.stream()
                    .collect(Collectors.groupingBy(MensajeChat::getConversacionId, Collectors.counting()));
            unreadMap.putAll(tempMap);
        }

        // 5. Build contact list
        return contactoIds.stream()
                .map(id -> usuariosMap.get(id))
                .filter(Objects::nonNull)
                .map(u -> {
                    String avatar = "https://ui-avatars.com/api/?name="
                            + u.getNombre().replace(" ", "+")
                            + "&background=" + ("proveedor".equals(u.getRol()) ? "0d6efd" : "198754")
                            + "&color=fff&size=128";
                    String label = "proveedor".equals(u.getRol()) ? "Proveedor" : "Cliente";

                    long cid = calcularConversacionId(yo.getId(), u.getId());
                    long noLeidos = unreadMap.getOrDefault(cid, 0L);

                    ContactoConversacion c = new ContactoConversacion(
                            u.getId(), u.getNombre(), label, avatar, "", "", (int) noLeidos);

                    MensajeChat lastMsg = lastMessageMap.get(cid);
                    if (lastMsg != null) {
                        c.setUltimaVistaPrevia(vistaPrevia(lastMsg.getTexto()));
                        c.setUltimoTiempoRelativo(tiempoRelativo(lastMsg.getCreadoEn()));
                    }

                    return c;
                })
                .sorted((a, b) -> {
                    if (a.getMensajesNoLeidos() != b.getMensajesNoLeidos()) {
                        return b.getMensajesNoLeidos() - a.getMensajesNoLeidos();
                    }
                    boolean aHasPreview = a.getUltimaVistaPrevia() != null && !a.getUltimaVistaPrevia().isEmpty();
                    boolean bHasPreview = b.getUltimaVistaPrevia() != null && !b.getUltimaVistaPrevia().isEmpty();
                    if (aHasPreview != bHasPreview) return aHasPreview ? -1 : 1;
                    return 0;
                })
                .collect(Collectors.toList());
    }

    private long extraerOtroUserId(long conversacionId, long userId) {
        long a = conversacionId / 10000L;
        long b = conversacionId % 10000L;
        if (a == userId) return b;
        if (b == userId) return a;
        return -1;
    }

    public void marcarLeidos(Long userId, Long otroUsuarioId) {
        long cid = calcularConversacionId(userId, otroUsuarioId);
        List<MensajeChat> noLeidos = mensajeChatRepository
                .findByConversacionIdAndSenderUserIdNotAndLeidoFalse(cid, userId);
        if (!noLeidos.isEmpty()) {
            noLeidos.forEach(m -> m.setLeido(true));
            mensajeChatRepository.saveAll(noLeidos);
        }
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