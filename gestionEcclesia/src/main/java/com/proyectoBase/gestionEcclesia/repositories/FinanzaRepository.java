package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.Finanza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinanzaRepository extends JpaRepository<Finanza, Long> {
    //List<Finanza> findByListaGastosAndListaEntradasAndFechaInicialReporteAndFechaFinalReporteBetween(LocalDate fechaInicialReporte, LocalDate fechaFinalReporteBefore);
    //List<Finanza> findByListaEntradasAndFechaInicialReporteAndFechaFinalReporteBetween(LocalDate fechaInicialReporte, LocalDate fechaFinalReporteBefore);

    @Query("SELECT f FROM Finanza f WHERE f.fechaInicialReporte >= :fechaInicio AND f.fechaFinalReporte <= :fechaFin")
    List<Finanza> findByRangoFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}

