package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.SolicitudServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudServicioRepository extends JpaRepository<SolicitudServicio, Long> {
    List<SolicitudServicio> findByClienteIdOrderByCreadoEnDesc(Long clienteId);

    @Query("SELECT s FROM SolicitudServicio s WHERE s.servicioId IN :servicioIds ORDER BY s.creadoEn DESC")
    List<SolicitudServicio> findByServicioIdsOrderByCreadoEnDesc(@Param("servicioIds") List<Long> servicioIds);

    @Query("SELECT COUNT(s) > 0 FROM SolicitudServicio s WHERE s.servicioId = :servicioId AND s.clienteId = :clienteId AND s.estado NOT IN ('COMPLETADO', 'CANCELADO')")
    boolean existsActivaByServicioIdAndClienteId(@Param("servicioId") Long servicioId, @Param("clienteId") Long clienteId);
}
