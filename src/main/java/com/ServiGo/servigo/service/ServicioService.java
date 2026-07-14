package com.ServiGo.servigo.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.repository.ServicioRepository;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> buscar(String q, String categoria) {
        return servicioRepository.search(q, categoria);
    }

    public void ordenar(List<Servicio> servicios, String ordenar) {
        if (ordenar == null) return;
        switch (ordenar) {
            case "precio-menor" -> servicios.sort(Comparator.comparing(Servicio::getPrecio));
            case "precio-mayor" -> servicios.sort(Comparator.comparing(Servicio::getPrecio).reversed());
            case "mejor-calificacion" -> servicios.sort(Comparator.comparing(Servicio::getCalificacion).reversed());
            default -> {}
        }
    }

    public List<Servicio> obtenerTendencias() {
        return servicioRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Servicio::getCategoria,
                        s -> s,
                        (a, b) -> a.getCalificacion() >= b.getCalificacion() ? a : b))
                .values().stream()
                .sorted(Comparator.comparing(Servicio::getCategoria))
                .collect(Collectors.toList());
    }

    public List<String> obtenerCategorias() {
        return servicioRepository.findDistinctCategoria();
    }

    public Optional<Servicio> findById(Long id) {
        return servicioRepository.findById(id);
    }

    public List<Servicio> obtenerPorIds(List<Long> ids) {
        return ids.isEmpty() ? List.of() : servicioRepository.findAllById(ids);
    }

    public List<Servicio> obtenerPorProveedor(Long proveedorId) {
        return servicioRepository.findByProveedorIdOrderByCalificacionDesc(proveedorId);
    }

    public Servicio crear(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public Servicio editar(Long id, Servicio datos) {
        Servicio existente = servicioRepository.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setCategoria(datos.getCategoria());
        existente.setPrecio(datos.getPrecio());
        existente.setImagen(datos.getImagen());
        existente.setDisponible(datos.getDisponible());
        return servicioRepository.save(existente);
    }

    public boolean eliminar(Long id, Long proveedorId) {
        Servicio existente = servicioRepository.findById(id).orElse(null);
        if (existente == null) return false;
        if (existente.getProveedorId() == null || !existente.getProveedorId().equals(proveedorId)) return false;
        servicioRepository.deleteById(id);
        return true;
    }
}
