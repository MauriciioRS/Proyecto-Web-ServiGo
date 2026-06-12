package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuarioId(Long usuarioId);
    Optional<Favorito> findByUsuarioIdAndServicioId(Long usuarioId, Long servicioId);
    boolean existsByUsuarioIdAndServicioId(Long usuarioId, Long servicioId);
}
