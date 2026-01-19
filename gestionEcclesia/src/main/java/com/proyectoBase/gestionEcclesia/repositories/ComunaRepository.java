package com.proyectoBase.gestionEcclesia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyectoBase.gestionEcclesia.modele.Comuna;

import java.util.List;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {
    List<Comuna> findByNombre(String nombre);


}
