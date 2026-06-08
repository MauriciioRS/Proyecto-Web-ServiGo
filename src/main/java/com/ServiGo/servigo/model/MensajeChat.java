package com.ServiGo.servigo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mensaje en una conversación demo. {@code delProveedor=true} = burbuja izquierda (profesional).
 */
@Entity
@Table(name = "mensajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeChat {
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long conversacionId;
    private boolean delProveedor;
    private String texto;
    private LocalDateTime creadoEn;

    public String getHoraCorta() {
        if (creadoEn == null) {
            return "";
        }
        if (Duration.between(creadoEn, LocalDateTime.now()).toMinutes() < 2) {
            return "Ahora";
        }
        return creadoEn.format(HORA);
    }
}
