package com.ServiGo.servigo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.model.SolicitudServicio;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.CalificacionService;
import com.ServiGo.servigo.service.ServicioService;
import com.ServiGo.servigo.service.SolicitudService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tecnico")
public class ProveedorController {

    private final ServicioService servicioService;
    private final SolicitudService solicitudService;
    private final CalificacionService calificacionService;

    private static final int LIMITE_SERVICIOS_NORMAL = 5;
    private static final int LIMITE_ACEPTADOS_NORMAL = 3;

    public ProveedorController(ServicioService servicioService,
                               SolicitudService solicitudService,
                               CalificacionService calificacionService) {
        this.servicioService = servicioService;
        this.solicitudService = solicitudService;
        this.calificacionService = calificacionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        List<SolicitudServicio> todasSolicitudes = solicitudService.obtenerPorProveedor(usuario.getId());
        List<SolicitudServicio> solicitudesActivas = solicitudService.filtrarActivas(todasSolicitudes);
        List<Servicio> misServicios = servicioService.obtenerPorProveedor(usuario.getId());

        long pendientes = todasSolicitudes.stream().filter(s -> "PENDIENTE".equals(s.getEstado())).count();
        long aceptadas = todasSolicitudes.stream().filter(s -> "ACEPTADO".equals(s.getEstado())).count();
        long enProgreso = todasSolicitudes.stream().filter(s -> "EN_PROGRESO".equals(s.getEstado())).count();
        long completadas = todasSolicitudes.stream().filter(s -> "COMPLETADO".equals(s.getEstado())).count();

        Map<Long, Long> chatMap = new HashMap<>();
        for (SolicitudServicio s : todasSolicitudes) {
            chatMap.put(s.getId(), s.getClienteId());
        }

        model.addAttribute("todasSolicitudes", todasSolicitudes);
        model.addAttribute("solicitudesActivas", solicitudesActivas);
        model.addAttribute("misServicios", misServicios);
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("aceptadas", aceptadas);
        model.addAttribute("enProgreso", enProgreso);
        model.addAttribute("completadas", completadas);
        model.addAttribute("promedioCalificacion", calificacionService.promedioProveedor(usuario.getId()));
        model.addAttribute("chatMap", chatMap);
        return "dashboard-proveedor";
    }

    @GetMapping("/servicios")
    public String misServicios(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        List<Servicio> servicios = servicioService.obtenerPorProveedor(usuario.getId());
        model.addAttribute("misServicios", servicios);
        return "mis-servicios-proveedor";
    }

    @GetMapping("/servicios/crear")
    public String crearServicioForm(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        model.addAttribute("categorias", servicioService.obtenerCategorias());
        model.addAttribute("servicio", new Servicio());
        return "crear-servicio";
    }

    @PostMapping("/servicios/crear")
    public String crearServicio(@ModelAttribute Servicio servicio,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        if (!Boolean.TRUE.equals(usuario.getPremium())) {
            long count = servicioService.obtenerPorProveedor(usuario.getId()).size();
            if (count >= LIMITE_SERVICIOS_NORMAL) {
                redirectAttributes.addFlashAttribute("error",
                        "Has alcanzado el límite de " + LIMITE_SERVICIOS_NORMAL + " servicios. " +
                        "Hazte Premium para publicar servicios ilimitados.");
                return "redirect:/premium";
            }
        }

        servicio.setProveedorId(usuario.getId());
        servicio.setDisponible(true);
        if (servicio.getCalificacion() == null) servicio.setCalificacion(0);
        servicioService.crear(servicio);
        redirectAttributes.addFlashAttribute("success", "Servicio creado exitosamente.");
        return "redirect:/tecnico/servicios";
    }

    @GetMapping("/servicios/{id}/editar")
    public String editarServicioForm(@PathVariable Long id,
                                     HttpSession session,
                                     Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        var servicio = servicioService.findById(id);
        if (servicio.isEmpty() || !servicio.get().getProveedorId().equals(usuario.getId())) {
            return "redirect:/tecnico/servicios";
        }

        model.addAttribute("categorias", servicioService.obtenerCategorias());
        model.addAttribute("servicio", servicio.get());
        return "crear-servicio";
    }

    @PostMapping("/servicios/{id}/editar")
    public String editarServicio(@PathVariable Long id,
                                 @ModelAttribute Servicio servicio,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        servicioService.editar(id, servicio);
        redirectAttributes.addFlashAttribute("success", "Servicio actualizado.");
        return "redirect:/tecnico/servicios";
    }

    @PostMapping("/servicios/{id}/eliminar")
    public String eliminarServicio(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        boolean eliminado = servicioService.eliminar(id, usuario.getId());
        if (eliminado) {
            redirectAttributes.addFlashAttribute("success", "Servicio eliminado.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el servicio.");
        }
        return "redirect:/tecnico/servicios";
    }

    @GetMapping("/calificaciones")
    public String calificaciones(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/iniciar-sesion";

        model.addAttribute("calificaciones", calificacionService.obtenerPorProveedor(usuario.getId()));
        model.addAttribute("promedio", calificacionService.promedioProveedor(usuario.getId()));
        return "calificaciones-proveedor";
    }
}