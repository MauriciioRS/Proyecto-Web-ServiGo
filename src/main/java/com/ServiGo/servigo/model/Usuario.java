package com.ServiGo.servigo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String email;
    private String telefono;
    private String dni;
    private String fechaNacimiento;
    private String direccion;
    private String distrito;
    private String rol; // "cliente" o "proveedor"
    private String imagen;
    private Boolean activo;
    private String password;
}
