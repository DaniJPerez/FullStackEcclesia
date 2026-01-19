package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.RecursoEconomico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecursoEconomicoRepository extends JpaRepository<RecursoEconomico, Long> {
    //List<RecursoEconomico> findByFechaContribucionBetween(LocalDate inicio, LocalDate fin);
}
