package com.ServiGo.servigo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactoConversacion {
	private Long id;
	private String nombre;
	private String profesion;
	private String avatarUrl;
	private String ultimaVistaPrevia;
	private String ultimoTiempoRelativo;
	private int mensajesNoLeidos;
}
