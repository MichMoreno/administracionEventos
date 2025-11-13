package com.ipn.mx.administracioneventos.features.asistente.repository;

import com.ipn.mx.administracioneventos.core.domain.Asistente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AsistenteRepository extends JpaRepository<Asistente, Long> {

    // Consulta personalizada para todos los asistentes
    @Query("SELECT a FROM Asistente a")
    List<Asistente> findAllAsistentes();

    // Consulta personalizada por ID
    @Query("SELECT a FROM Asistente a WHERE a.idAsistente = :id")
    Optional<Asistente> findByIdAsistentes(@Param("id") Long id);

    // Método para eliminar (usando el estándar de JPA)
    void deleteByIdAsistente(Long id);

    // Métodos derivados automáticos (opcionales)
    List<Asistente> findByNombreContaining(String nombre);
    List<Asistente> findByEmail(String email);
}