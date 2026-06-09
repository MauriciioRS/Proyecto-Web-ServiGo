package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.Servicio;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    default List<Servicio> search(String q, String categoria) {
        String normalizedQuery = normalize(q);
        String normalizedCategoria = normalize(categoria);

        return findAll().stream()
                .filter(servicio -> normalizedCategoria == null || normalizedCategoria.isBlank()
                        || normalize(servicio.getCategoria()).equals(normalizedCategoria))
                .filter(servicio -> normalizedQuery == null || normalizedQuery.isBlank()
                        || normalize(servicio.getNombre()).contains(normalizedQuery)
                        || normalize(servicio.getDescripcion()).contains(normalizedQuery))
                .collect(Collectors.toList());
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.toLowerCase(Locale.ROOT)
                .replace('á', 'a')
                .replace('é', 'e')
                .replace('í', 'i')
                .replace('ó', 'o')
                .replace('ú', 'u')
                .replace('ü', 'u')
                .replace('ñ', 'n');
    }

    @Query("SELECT DISTINCT s.categoria FROM Servicio s ORDER BY s.categoria")
    List<String> findDistinctCategoria();
}
