package com.ServiGo.servigo.controller;

import com.ServiGo.servigo.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionController {
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @GetMapping
    public String listaNotificaciones(Model model) {
        model.addAttribute("notificaciones", notificacionRepository.findByUsuarioId(1L));
        return "notificaciones";
    }
}
