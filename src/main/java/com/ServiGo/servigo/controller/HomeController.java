package com.ServiGo.servigo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.NotificacionService;
import com.ServiGo.servigo.service.ServicioService;
import com.ServiGo.servigo.service.SolicitudService;

@Controller
public class HomeController {
    
    private final ServicioService servicioService;
    private final NotificacionService notificacionService;
    private final SolicitudService solicitudService;
    
    public HomeController(ServicioService servicioService,
                          NotificacionService notificacionService,
                          SolicitudService solicitudService) {
        this.servicioService = servicioService;
        this.notificacionService = notificacionService;
        this.solicitudService = solicitudService;
    }
    
    @GetMapping("/")
    public String inicio(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categoria,
            @RequestParam(name = "orden", required = false) String ordenar,
            HttpSession session,
            Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        boolean esProveedor = usuarioLogueado != null && "proveedor".equals(usuarioLogueado.getRol());

        List<Servicio> servicios = servicioService.buscar(q, categoria);
        servicioService.ordenar(servicios, ordenar);

        List<Servicio> tendencias = servicioService.obtenerTendencias();

        int noLeidas = (usuarioLogueado != null)
                ? notificacionService.contarNoLeidas(usuarioLogueado.getId())
                : 0;

        model.addAttribute("servicios", servicios);
        model.addAttribute("tendencias", tendencias);
        model.addAttribute("notificacionesNoLeidas", noLeidas);
        model.addAttribute("categorias", servicioService.obtenerCategorias());
        model.addAttribute("query", q);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("ordenSeleccionado", ordenar);
        model.addAttribute("esProveedor", esProveedor);

        if (esProveedor) {
            List<SolicitudServicio> todasSolicitudes = solicitudService.obtenerPorProveedor(usuarioLogueado.getId());
            List<SolicitudServicio> solicitudesActivas = solicitudService.filtrarActivas(todasSolicitudes);
            long pendientes = solicitudesActivas.stream().filter(s -> "PENDIENTE".equals(s.getEstado())).count();
            long enProgreso = solicitudesActivas.stream().filter(s -> !"PENDIENTE".equals(s.getEstado())).count();
            long completadas = todasSolicitudes.stream().filter(s -> "COMPLETADO".equals(s.getEstado())).count();
            List<Servicio> misServicios = servicioService.obtenerPorProveedor(usuarioLogueado.getId());

            model.addAttribute("solicitudesActivas", solicitudesActivas);
            model.addAttribute("pendientes", pendientes);
            model.addAttribute("enProgreso", enProgreso);
            model.addAttribute("completadas", completadas);
            model.addAttribute("totalServicios", misServicios.size());
        }

        return "inicio";
    }
    
    @GetMapping("/home")
    public String home(Model model) {
        return "redirect:/";
    }

}
