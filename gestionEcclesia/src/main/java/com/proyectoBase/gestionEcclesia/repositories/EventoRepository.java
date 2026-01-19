package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.Evento;
import com.proyectoBase.gestionEcclesia.modele.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByFechaInicioAfter(LocalDateTime fecha);
    List<Evento> findByFechaInicioBetween(LocalDateTime inicio, LocalDateTime fin);
}
