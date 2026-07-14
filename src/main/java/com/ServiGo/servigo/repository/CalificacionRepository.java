package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByProveedorIdOrderByCreadoEnDesc(Long proveedorId);
    List<Calificacion> findByClienteIdOrderByCreadoEnDesc(Long clienteId);
    Optional<Calificacion> findBySolicitudId(Long solicitudId);
    boolean existsBySolicitudId(Long solicitudId);
}
