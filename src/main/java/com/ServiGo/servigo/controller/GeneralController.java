package com.ServiGo.servigo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    @GetMapping("/contactanos")
    public String contactanos() {
        return "contactanos";
    }

    @GetMapping("/quienes-somos")
    public String quienesSomos() {
        return "quienes-somos";
    }

    @GetMapping("/favoritos")
    public String favoritos() {
        return "favoritos";
    }

    @GetMapping("/mensajes")
    public String mensajes() {
        return "mensajes";
    }
}
