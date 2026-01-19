package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.EntidadTerritorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadTerritorialRepository extends JpaRepository<EntidadTerritorial, Long> {
}
