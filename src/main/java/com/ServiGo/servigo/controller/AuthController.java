package com.ServiGo.servigo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @GetMapping("/iniciar-sesion")
    public String iniciarSesion() {
        return "iniciar-sesion";
    }

    @GetMapping("/cambiar-contrasena")
    public String cambiarContrasena() {
        return "cambiar-contrasena";
    }
}
