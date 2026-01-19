package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiembroRepository extends JpaRepository<Persona, Long>{
    List<Persona> findByPrimerNombreContainingIgnoreCase(String primerNombre);
    Optional<Persona> findByCorreo(String correo);

}
