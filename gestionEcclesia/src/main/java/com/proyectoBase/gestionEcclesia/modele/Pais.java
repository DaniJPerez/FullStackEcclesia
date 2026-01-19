package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name= "paises", uniqueConstraints = @UniqueConstraint(name= "uk_nombre_pais", columnNames = {"nombre_pais"}))
public class Pais {
    @Id
    @Column(name= "id_pais", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pais_seq")
    @SequenceGenerator(
            name = "pais_seq",
            sequenceName = "SEC_PAIS", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name= "nombre_pais", length = 35, nullable = false)
    private String nombrePais;

    //@Column(name= "nombre_nacionalidad", length = 35, nullable = false)
    //private String nombreNacionalidad;

    @Column(name= "descripcion_pais", length = 55)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_pais_usuario_registro"))
    private Usuario usuarioRegirstro;
}

