package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.modele.Evento;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContribucionRepository extends JpaRepository<Contribucion, Long> {
    List<Contribucion> findByPersona(Persona persona);
    List<Contribucion> findByFechaContribucionBetween(LocalDate inicio, LocalDate fin);
    List<Contribucion> findByEvento(Evento evento);
}
