package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.ContactoConversacion;
import com.ServiGo.servigo.model.MensajeChat;
import com.ServiGo.servigo.repository.MensajeChatRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mensajes")
public class MensajeController {

	@Autowired
	private MensajeChatRepository mensajeChatRepository;

	@GetMapping
	public String ver(
			HttpSession session,
			@RequestParam(name = "c", defaultValue = "1") Long conversacionId,
			Model model) {
		if (session.getAttribute("usuarioLogueado") == null) {
			return "redirect:/iniciar-sesion";
		}

		List<ContactoConversacion> contactos = buildContactos();
		long cid = contactos.stream().anyMatch(x -> x.getId().equals(conversacionId))
				? conversacionId
				: 1L;

		ContactoConversacion actual = contactos.stream()
				.filter(x -> x.getId().equals(cid))
				.findFirst()
				.orElse(contactos.get(0));

		model.addAttribute("contactos", contactos);
		model.addAttribute("conversacionId", cid);
		model.addAttribute("contactoActual", actual);
		model.addAttribute("mensajes", mensajeChatRepository.findByConversacionIdOrderByCreadoEnAsc(cid));
		return "mensajes";
	}

	@PostMapping("/enviar")
	public String enviar(
			HttpSession session,
			@RequestParam("conversacionId") Long conversacionId,
			@RequestParam("texto") String texto) {
		if (session.getAttribute("usuarioLogueado") == null) {
			return "redirect:/iniciar-sesion";
		}
		if (texto == null || texto.isBlank()) {
			return "redirect:/mensajes?c=" + conversacionId;
		}
		String limpio = texto.trim();
		if (limpio.length() > 2000) {
			limpio = limpio.substring(0, 2000);
		}
		MensajeChat m = new MensajeChat(null, conversacionId, false, limpio, LocalDateTime.now());
		mensajeChatRepository.save(m);
		return "redirect:/mensajes?c=" + conversacionId;
	}

	private List<ContactoConversacion> buildContactos() {
		List<ContactoConversacion> lista = new ArrayList<>();
		lista.add(new ContactoConversacion(1L, "Carlos Ruiz", "Electricista",
				"https://ui-avatars.com/api/?name=Carlos+Ruiz&background=0d6efd&color=fff&size=128", "", ""));
		lista.add(new ContactoConversacion(2L, "María García", "Plomera",
				"https://ui-avatars.com/api/?name=Maria+Garcia&background=198754&color=fff&size=128", "", ""));
		lista.add(new ContactoConversacion(3L, "Juan Rodríguez", "Pintor",
				"https://ui-avatars.com/api/?name=Juan+Rodriguez&background=fd7e14&color=fff&size=128", "", ""));
		for (ContactoConversacion c : lista) {
			mensajeChatRepository.findTopByConversacionIdOrderByCreadoEnDesc(c.getId()).ifPresent(m -> {
				c.setUltimaVistaPrevia(vistaPrevia(m.getTexto()));
				c.setUltimoTiempoRelativo(tiempoRelativo(m.getCreadoEn()));
			});
		}
		return lista;
	}

	private static String vistaPrevia(String texto) {
		if (texto == null) {
			return "";
		}
		String t = texto.replaceAll("\\s+", " ").trim();
		if (t.length() <= 56) {
			return t;
		}
		return t.substring(0, 53) + "…";
	}

	private static String tiempoRelativo(LocalDateTime cuando) {
		if (cuando == null) {
			return "";
		}
		Duration d = Duration.between(cuando, LocalDateTime.now());
		if (d.isNegative()) {
			return "Ahora";
		}
		long mins = d.toMinutes();
		if (mins < 1) {
			return "Ahora";
		}
		if (mins < 60) {
			return mins + "m";
		}
		long hours = d.toHours();
		if (hours < 24) {
			return hours + "h";
		}
		long days = d.toDays();
		if (days < 7) {
			return days + "d";
		}
		return cuando.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM"));
	}
}
