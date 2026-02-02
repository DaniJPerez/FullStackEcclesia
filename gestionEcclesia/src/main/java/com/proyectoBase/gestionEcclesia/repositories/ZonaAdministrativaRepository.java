package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.ZonaAdministrativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaAdministrativaRepository extends JpaRepository<ZonaAdministrativa, Long> {
    ZonaAdministrativa findByNombreZona(String nombreZona);

}
