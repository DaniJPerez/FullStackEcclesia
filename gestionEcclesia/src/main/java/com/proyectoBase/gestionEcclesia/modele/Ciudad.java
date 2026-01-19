package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "ciudades", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_nombre_ciudad_entidad_territorial",
                columnNames = {"nombre_ciudad", "id_entidad_territorial"}
        )
})
public class Ciudad {
    @Id
    @Column(name= "id_ciudad")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ciudad_seq")
    @SequenceGenerator(
            name = "ciudad_seq",
            sequenceName = "SEC_CIUDAD", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long idCiudad;

    @Column(name="nombre_ciudad", length = 35, nullable = false)
    private String nombreCiudad;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidad_territorial", foreignKey = @ForeignKey(name = "fk_ciudad_entidad_territorial"))
    private EntidadTerritorial entidadTerritorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_ciudades_usuario_registro"))
    private Usuario usuarioRegirstro;
}
