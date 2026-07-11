package com.ServiGo.servigo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ServiGo.servigo.model.Favorito;
import com.ServiGo.servigo.model.Servicio;
import com.ServiGo.servigo.repository.FavoritoRepository;
import com.ServiGo.servigo.repository.ServicioRepository;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final ServicioRepository servicioRepository;

    public FavoritoService(FavoritoRepository favoritoRepository, ServicioRepository servicioRepository) {
        this.favoritoRepository = favoritoRepository;
        this.servicioRepository = servicioRepository;
    }

    public void toggle(Long usuarioId, Long servicioId) {
        if (favoritoRepository.existsByUsuarioIdAndServicioId(usuarioId, servicioId)) {
            Favorito fav = favoritoRepository
                    .findByUsuarioIdAndServicioId(usuarioId, servicioId).orElse(null);
            if (fav != null) favoritoRepository.delete(fav);
        } else {
            favoritoRepository.save(new Favorito(null, usuarioId, servicioId));
        }
    }

    public boolean esFavorito(Long usuarioId, Long servicioId) {
        return favoritoRepository.existsByUsuarioIdAndServicioId(usuarioId, servicioId);
    }

    public List<Servicio> obtenerFavoritos(Long usuarioId) {
        List<Favorito> favs = favoritoRepository.findByUsuarioId(usuarioId);
        List<Long> ids = favs.stream().map(Favorito::getServicioId).collect(Collectors.toList());
        return ids.isEmpty() ? List.of() : servicioRepository.findAllById(ids);
    }
}
