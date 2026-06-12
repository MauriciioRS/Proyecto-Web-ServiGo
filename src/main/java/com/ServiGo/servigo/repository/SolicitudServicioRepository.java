package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.SolicitudServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudServicioRepository extends JpaRepository<SolicitudServicio, Long> {
    List<SolicitudServicio> findByClienteIdOrderByCreadoEnDesc(Long clienteId);
}
