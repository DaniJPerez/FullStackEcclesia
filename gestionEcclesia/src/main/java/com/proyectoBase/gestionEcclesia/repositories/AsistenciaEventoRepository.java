package com.proyectoBase.gestionEcclesia.repositories;

import com.proyectoBase.gestionEcclesia.modele.AsistenciaEvento;
import com.proyectoBase.gestionEcclesia.modele.Evento;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaEventoRepository extends JpaRepository<AsistenciaEvento, Long> {
    // Aquí puedes agregar métodos específicos para la entidad AsistenciaEvento si es necesario
    // Por ejemplo, puedes agregar métodos de consulta personalizados utilizando JPQL o SQL nativo
    // Llama al procedimiento PL/SQL para obtener los miembros por fecha

    @Procedure(procedureName = "SP_GET_MIEMBROS_BY_FECHA")
    List<Persona> findMiembrosByFechaEventoProcedure(@Param("fecha") LocalDate fecha, Long idEvento);

    List<AsistenciaEvento> findByPersona_NumeroIdentificacion(Long miembroId);

    List<AsistenciaEvento> findByEvento_Id(Long eventoId);

    void deleteByPersona(Persona persona);

    AsistenciaEvento findByPersonaAndEvento(Persona persona, Evento evento);

    List<AsistenciaEvento> findByPersona(Persona persona);

    List<AsistenciaEvento> findByEvento(Evento evento);

}

