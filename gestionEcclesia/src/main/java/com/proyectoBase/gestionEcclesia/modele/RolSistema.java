package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.Embeddable;

@Embeddable
public enum RolSistema {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_CONSULTA
}
