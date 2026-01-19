package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.ZonaAbministrativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaAbministrativaRepository extends JpaRepository<ZonaAbministrativa, Long> {
    ZonaAbministrativa fyndByZonaAbministrativa(ZonaAbministrativa zona);

}
