package com.proyectoBase.gestionEcclesia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyectoBase.gestionEcclesia.modele.Ciudad;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    //public List<Ciudad> findByCiudad(Ciudad ciudad);
    public List<Ciudad> findByNombreCiudad(String nombre);
}
