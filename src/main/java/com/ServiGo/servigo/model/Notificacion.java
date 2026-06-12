package com.ServiGo.servigo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    private String mensaje;
    private String tipo; // "alerta", "oferta", "mensaje"
    private LocalDateTime fecha;
    private Boolean leida;
    private Long usuarioId;
    private Long solicitudId; // nullable — referencia al flujo de trabajo
}
