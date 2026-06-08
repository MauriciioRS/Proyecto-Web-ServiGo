package com.ServiGo.servigo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ServiGo.servigo.repository.NotificacionRepository;
import com.ServiGo.servigo.repository.ServicioRepository;

@Controller
public class HomeController {
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        model.addAttribute("notificacionesNoLeidas", notificacionRepository.findByUsuarioIdAndLeidaFalse(1L).size());
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
