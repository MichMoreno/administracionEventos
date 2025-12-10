package com.ipn.mx.administracioneventos.features.evento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ipn.mx.administracioneventos.core.domain.Evento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    //Crear consulta en JPQL que devuelva un evento por nombre

    @Query("SELECT e FROM Evento e WHERE e.nombreEvento = :nombre")
    Optional<Evento> findByNombreEvento(@Param("nombre") String nombre);


    List<Evento> findByNombreEventoContaining(String nombre);

    @Query("SELECT e FROM Evento e WHERE e.fechaInicio BETWEEN :inicio AND :fin ORDER BY e.fechaInicio")
    List<Evento> findEventosPorRangoFechas(@Param("inicio") Date inicio, @Param("fin") Date fin);
}
