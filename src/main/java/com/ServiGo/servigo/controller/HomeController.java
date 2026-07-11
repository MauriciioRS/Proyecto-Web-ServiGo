package com.ServiGo.servigo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.NotificacionService;
import com.ServiGo.servigo.service.ServicioService;

@Controller
public class HomeController {
    
    private final ServicioService servicioService;
    private final NotificacionService notificacionService;
    
    public HomeController(ServicioService servicioService, NotificacionService notificacionService) {
        this.servicioService = servicioService;
        this.notificacionService = notificacionService;
    }
    
    @GetMapping("/")
    public String inicio(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categoria,
            @RequestParam(name = "orden", required = false) String ordenar,
            HttpSession session,
            Model model) {
        List<Servicio> servicios = servicioService.buscar(q, categoria);
        servicioService.ordenar(servicios, ordenar);

        List<Servicio> tendencias = servicioService.obtenerTendencias();

        model.addAttribute("servicios", servicios);
        model.addAttribute("tendencias", tendencias);
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        int noLeidas = (usuarioLogueado != null)
                ? notificacionService.contarNoLeidas(usuarioLogueado.getId())
                : 0;
        model.addAttribute("notificacionesNoLeidas", noLeidas);
        model.addAttribute("categorias", servicioService.obtenerCategorias());
        model.addAttribute("query", q);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("ordenSeleccionado", ordenar);
        return "inicio";
    }
    
    @GetMapping("/home")
    public String home(Model model) {
        return "redirect:/";
    }

    @GetMapping("/premium")
    public String premium() {
        return "suscripcion";
    }   
}
