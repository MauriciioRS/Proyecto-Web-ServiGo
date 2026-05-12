package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.Servicio;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ServicioRepository {
    
    private static List<Servicio> servicios = new ArrayList<>();
    private static Long idCounter = 1L;
    
    static {
        // Datos sintéticos de prueba
        servicios.add(new Servicio(1L, "Reparación de Plomería", "Reparación y mantenimiento de tuberías", "Plomería", 50.0, "plomeria.jpg", true, 4));
        servicios.add(new Servicio(2L, "Limpieza del Hogar", "Servicio de limpieza profunda", "Limpieza", 35.0, "limpiezadelHogar.JPG", true, 5));
        servicios.add(new Servicio(3L, "Electricista", "Reparación de instalaciones eléctricas", "Electricidad", 60.0, "Electricista.JPG", true, 4));
        servicios.add(new Servicio(4L, "Jardinería", "Mantenimiento de jardines y plantas", "Jardinería", 40.0, "Jardineria.jpg", true, 3));
        servicios.add(new Servicio(5L, "Reparación de Electrodomésticos", "Arreglo de neveras, lavadoras, etc", "Electrodomésticos", 70.0, "ReparaciondeElectrodomesticos.jpg", true, 4));
        servicios.add(new Servicio(6L, "Pintura", "Pintura de interiores y exteriores", "Pintura", 45.0, "Pintor.jpg", true, 5));
        idCounter = 7L;
    }
    
    public List<Servicio> findAll() {
        return new ArrayList<>(servicios);
    }
    
    public Optional<Servicio> findById(Long id) {
        return servicios.stream().filter(s -> s.getId().equals(id)).findFirst();
    }
    
    public Servicio save(Servicio servicio) {
        if (servicio.getId() == null) {
            servicio.setId(idCounter++);
        }
        servicios.removeIf(s -> s.getId().equals(servicio.getId()));
        servicios.add(servicio);
        return servicio;
    }
    
    public void deleteById(Long id) {
        servicios.removeIf(s -> s.getId().equals(id));
    }
}
