package com.proyectoBase.gestionEcclesia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyectoBase.gestionEcclesia.modele.Barrio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarrioRepository extends JpaRepository<Barrio, Long> {
    Optional<Barrio> findByNombre(String nombreBarrio);
}
