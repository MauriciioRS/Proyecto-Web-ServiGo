package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/servicios")
public class ServicioController {
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @GetMapping
    public String listaServicios(Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        return "servicios";
    }
    
    @GetMapping("/{id}")
    public String detalleServicio(@PathVariable Long id, Model model) {
        var servicio = servicioRepository.findById(id);
        if (servicio.isPresent()) {
            model.addAttribute("servicio", servicio.get());
            return "servicio-detalle";
        }
        return "redirect:/servicios";
    }
}
