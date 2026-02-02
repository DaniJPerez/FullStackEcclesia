package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="zona_abministrativa", uniqueConstraints = @UniqueConstraint(name= "uk_zona_nombre", columnNames = {"nombre_zona"}))
public class ZonaAdministrativa {
    @Id
    @Column(name="id_zona", length = 10, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zona_abministrativa_seq")
    @SequenceGenerator(
            name = "zona_abministrativa_seq",
            sequenceName = "SEC_ZONA_ABMINISTRATIVA", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long idZona;

    @Column(name="nombre_zona", length = 35, nullable = false)
    private String nombreZona;

    @Column(name = "descripcion_zona", length = 50)
    private String descripcion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lider_zona", foreignKey = @ForeignKey(name = "fk_zona_lider"))
    private Persona LiderZona; //presvitero o pastor encargado de la zona

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_zona_abministrativa_usuario_registro"))
    private Usuario usuarioRegirstro;

}
