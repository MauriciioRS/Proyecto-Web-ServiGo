package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.repository.ServicioRepository;
import com.ServiGo.servigo.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
