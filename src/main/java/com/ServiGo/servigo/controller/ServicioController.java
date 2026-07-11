package com.ServiGo.servigo.controller;

import java.util.List;

import com.ServiGo.servigo.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.service.FavoritoService;
import com.ServiGo.servigo.service.ServicioService;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;
    private final FavoritoService favoritoService;

    public ServicioController(ServicioService servicioService, FavoritoService favoritoService) {
        this.servicioService = servicioService;
        this.favoritoService = favoritoService;
    }

    @GetMapping
    public String listaServicios(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categoria,
            @RequestParam(name = "orden", required = false) String ordenar,
            Model model) {
        List<Servicio> servicios = servicioService.buscar(q, categoria);
        servicioService.ordenar(servicios, ordenar);

        model.addAttribute("servicios", servicios);
        model.addAttribute("categorias", servicioService.obtenerCategorias());
        model.addAttribute("query", q);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("ordenSeleccionado", ordenar);
        return "servicios";
    }

    @GetMapping("/{id}")
    public String detalleServicio(@PathVariable Long id, HttpSession session, Model model) {
        var servicio = servicioService.findById(id);
        if (servicio.isEmpty()) return "redirect:/servicios";

        model.addAttribute("servicio", servicio.get());
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            model.addAttribute("esFavorito",
                    favoritoService.esFavorito(usuario.getId(), id));
        }
        return "servicio-detalle";
    }
}
