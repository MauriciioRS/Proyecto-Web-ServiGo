package com.ServiGo.servigo.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ServiGo.servigo.model.ContactoConversacion;
import com.ServiGo.servigo.model.MensajeChat;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.MensajeChatRepository;
import com.ServiGo.servigo.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mensajes")
public class MensajeController {

    @Autowired
    private MensajeChatRepository mensajeChatRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // conversacionId única entre dos usuarios: min(id1,id2)*10000 + max(id1,id2)
    private static long convId(long a, long b) {
        return Math.min(a, b) * 10000L + Math.max(a, b);
    }

    @GetMapping
    public String ver(
            HttpSession session,
            @RequestParam(name = "c", required = false) Long otroUsuarioId,
            Model model) {
        Usuario yo = (Usuario) session.getAttribute("usuarioLogueado");
        if (yo == null) return "redirect:/iniciar-sesion";

        List<ContactoConversacion> contactos = buildContactos(yo);
        if (contactos.isEmpty()) {
            model.addAttribute("contactos", contactos);
            model.addAttribute("mensajes", List.of());
            model.addAttribute("sinContactos", true);
            return "mensajes";
        }

        final Long requestedId = otroUsuarioId;
        boolean validContact = requestedId != null
                && contactos.stream().anyMatch(c -> c.getId().equals(requestedId));
        final long otroId = validContact ? requestedId : contactos.get(0).getId();
        ContactoConversacion actual = contactos.stream()
                .filter(c -> c.getId().equals(otroId)).findFirst()
                .orElse(contactos.get(0));

        long cid = convId(yo.getId(), otroId);
        List<MensajeChat> mensajes = mensajeChatRepository.findByConversacionIdOrderByCreadoEnAsc(cid);

        model.addAttribute("contactos", contactos);
        model.addAttribute("otroUsuarioId", otroId);
        model.addAttribute("conversacionId", cid);
        model.addAttribute("contactoActual", actual);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("miId", yo.getId());
        model.addAttribute("lastId", mensajes.isEmpty() ? 0 : mensajes.get(mensajes.size() - 1).getId());
        return "mensajes";
    }

    @PostMapping("/enviar")
    public String enviar(
            HttpSession session,
            @RequestParam("otroUsuarioId") Long otroUsuarioId,
            @RequestParam("texto") String texto) {
        Usuario yo = (Usuario) session.getAttribute("usuarioLogueado");
        if (yo == null) return "redirect:/iniciar-sesion";
        if (texto == null || texto.isBlank()) return "redirect:/mensajes?c=" + otroUsuarioId;

        String limpio = texto.trim();
        if (limpio.length() > 2000) limpio = limpio.substring(0, 2000);

        long cid = convId(yo.getId(), otroUsuarioId);
        MensajeChat m = new MensajeChat(null, cid, yo.getId(), false, limpio, LocalDateTime.now());
        mensajeChatRepository.save(m);
        return "redirect:/mensajes?c=" + otroUsuarioId;
    }

    // Polling AJAX: devuelve JSON de mensajes nuevos después de lastId
    @GetMapping("/nuevos")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> nuevos(
            HttpSession session,
            @RequestParam("c") Long otroUsuarioId,
            @RequestParam(name = "desde", defaultValue = "0") Long lastId) {
        Usuario yo = (Usuario) session.getAttribute("usuarioLogueado");
        if (yo == null) return ResponseEntity.status(401).build();

        long cid = convId(yo.getId(), otroUsuarioId);
        List<MensajeChat> nuevos = mensajeChatRepository
                .findByConversacionIdAndIdGreaterThanOrderByCreadoEnAsc(cid, lastId);

        List<Map<String, Object>> result = nuevos.stream().map(msg -> {
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("id", msg.getId());
            r.put("texto", msg.getTexto());
            r.put("hora", msg.getHoraCorta());
            r.put("mio", Objects.equals(msg.getSenderUserId(), yo.getId()));
            return r;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/start-with-provider")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> startWithProvider(HttpSession session) {
        Usuario yo = (Usuario) session.getAttribute("usuarioLogueado");
        if (yo == null) return ResponseEntity.status(401).build();

        Optional<Usuario> proveedor = usuarioRepository.findAll().stream()
                .filter(u -> "proveedor".equalsIgnoreCase(u.getRol()))
                .findFirst();

        if (proveedor.isEmpty()) return ResponseEntity.status(404).build();

        Map<String, Long> res = new LinkedHashMap<>();
        res.put("otroUsuarioId", proveedor.get().getId());
        return ResponseEntity.ok(res);
    }

    private List<ContactoConversacion> buildContactos(Usuario yo) {
        return usuarioRepository.findAll().stream()
                .filter(u -> !u.getId().equals(yo.getId()))
                .map(u -> {
                    String avatar = "https://ui-avatars.com/api/?name="
                            + u.getNombre().replace(" ", "+")
                            + "&background=0d6efd&color=fff&size=128";
                    ContactoConversacion c = new ContactoConversacion(
                            u.getId(), u.getNombre(), u.getRol(), avatar, "", "");
                    long cid = convId(yo.getId(), u.getId());
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
