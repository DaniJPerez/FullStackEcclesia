package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "nacionalidades", uniqueConstraints = @UniqueConstraint(name= "uk_nacionalidad_nombre", columnNames = {"nombre_nacionalidad"}))
public class Nacionalidad {
    @Id
    @Column(name = "id_nacionalidad", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nacionalidad_seq")
    @SequenceGenerator(
            name = "nacionalidad_seq",
            sequenceName = "SEC_NACIONALIDAD", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long idNacionalidad;

    @Column(name = "nombre_nacionalidad", length = 35, nullable = false)
    private String nombreNacionalidad;

    @Column(name = "descripcion_nacionalidad", length = 50)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pais", foreignKey = @ForeignKey(name = "fk_nacionalidad_pais"))
    private Pais pais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_nacionalidad_usuario_registro"))
    private Usuario usuarioRegirstro;

}
