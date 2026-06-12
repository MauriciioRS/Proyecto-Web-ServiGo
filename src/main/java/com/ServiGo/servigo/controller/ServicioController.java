package com.ServiGo.servigo.controller;

import java.util.Comparator;
import java.util.List;

import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.repository.FavoritoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.repository.ServicioRepository;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;

    @GetMapping
    public String listaServicios(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categoria,
            @RequestParam(name = "orden", required = false) String ordenar,
            Model model) {
        List<Servicio> servicios = servicioRepository.search(q, categoria);
        sortServicios(servicios, ordenar);

        model.addAttribute("servicios", servicios);
        model.addAttribute("categorias", servicioRepository.findDistinctCategoria());
        model.addAttribute("query", q);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("ordenSeleccionado", ordenar);
        return "servicios";
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

    @GetMapping("/{id}")
    public String detalleServicio(@PathVariable Long id, HttpSession session, Model model) {
        var servicio = servicioRepository.findById(id);
        if (servicio.isEmpty()) return "redirect:/servicios";

        model.addAttribute("servicio", servicio.get());
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            model.addAttribute("esFavorito",
                    favoritoRepository.existsByUsuarioIdAndServicioId(usuario.getId(), id));
        }
        return "servicio-detalle";
    }
}
