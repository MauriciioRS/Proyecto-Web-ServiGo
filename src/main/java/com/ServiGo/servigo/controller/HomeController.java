package com.ServiGo.servigo.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.repository.NotificacionRepository;
import com.ServiGo.servigo.repository.ServicioRepository;

@Controller
public class HomeController {
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @GetMapping("/")
    public String inicio(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categoria,
            @RequestParam(name = "orden", required = false) String ordenar,
            Model model) {
        List<Servicio> servicios = servicioRepository.search(q, categoria);
        sortServicios(servicios, ordenar);

        // One best-rated service per category for Tendencias
        List<Servicio> tendencias = servicioRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Servicio::getCategoria,
                        s -> s,
                        (a, b) -> a.getCalificacion() >= b.getCalificacion() ? a : b))
                .values().stream()
                .sorted(Comparator.comparing(Servicio::getCategoria))
                .collect(Collectors.toList());

        model.addAttribute("servicios", servicios);
        model.addAttribute("tendencias", tendencias);
        model.addAttribute("notificacionesNoLeidas", notificacionRepository.findByUsuarioIdAndLeidaFalse(1L).size());
        model.addAttribute("categorias", servicioRepository.findDistinctCategoria());
        model.addAttribute("query", q);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("ordenSeleccionado", ordenar);
        return "inicio";
    }
    
    private void sortServicios(List<Servicio> servicios, String ordenar) {
        if (ordenar == null) {
            return;
        }
        switch (ordenar) {
            case "precio-menor" -> servicios.sort(Comparator.comparing(Servicio::getPrecio));
            case "precio-mayor" -> servicios.sort(Comparator.comparing(Servicio::getPrecio).reversed());
            case "mejor-calificacion" -> servicios.sort(Comparator.comparing(Servicio::getCalificacion).reversed());
            default -> {}
        }
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
