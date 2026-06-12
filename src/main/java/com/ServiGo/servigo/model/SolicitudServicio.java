package com.ServiGo.servigo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private Long servicioId;
    private String servicioNombre;
    private String servicioCategoria;
    private Double servicioPrecio;

    // PENDIENTE → ACEPTADO → EN_PROGRESO → COMPLETADO  (o CANCELADO)
    private String estado;

    private LocalDateTime creadoEn;
    private LocalDateTime aceptadoEn;
    private LocalDateTime enProgresoEn;
    private LocalDateTime completadoEn;
    private LocalDateTime actualizadoEn;
}
