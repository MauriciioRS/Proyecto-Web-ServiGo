package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.ContactoConversacion;
import com.ServiGo.servigo.model.MensajeChat;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.MensajeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/mensajes")
public class MensajeController {

    private final MensajeService mensajeService;

    public MensajeController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @GetMapping
    public String ver(
            HttpSession session,
            @RequestParam(name = "c", required = false) Long otroUsuarioId,
            Model model) {
        Usuario yo = (Usuario) session.getAttribute("usuarioLogueado");
        if (yo == null) return "redirect:/iniciar-sesion";

        List<ContactoConversacion> contactos = mensajeService.buildContactos(yo);
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

        List<MensajeChat> mensajes = mensajeService.obtenerMensajes(yo.getId(), otroId);

        model.addAttribute("contactos", contactos);
        model.addAttribute("otroUsuarioId", otroId);
        model.addAttribute("conversacionId", mensajeService.calcularConversacionId(yo.getId(), otroId));
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

        mensajeService.enviar(yo.getId(), otroUsuarioId, texto);
        return "redirect:/mensajes?c=" + otroUsuarioId;
    }

    @GetMapping("/nuevos")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> nuevos(
            HttpSession session,
            @RequestParam("c") Long otroUsuarioId,
            @RequestParam(name = "desde", defaultValue = "0") Long lastId) {
        Usuario yo = (Usuario) session.getAttribute("usuarioLogueado");
        if (yo == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(mensajeService.obtenerNuevos(yo.getId(), otroUsuarioId, lastId));
    }
}
