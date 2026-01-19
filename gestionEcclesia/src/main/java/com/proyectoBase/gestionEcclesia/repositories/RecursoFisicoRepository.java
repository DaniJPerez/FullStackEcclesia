package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.RecursoFisico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoFisicoRepository extends JpaRepository<RecursoFisico, Long> {
    List<RecursoFisico> findByCategoriaContainingIgnoreCase(String descripcion);
}
