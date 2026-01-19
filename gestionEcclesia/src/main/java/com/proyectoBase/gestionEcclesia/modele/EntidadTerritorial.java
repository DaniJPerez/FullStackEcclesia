package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="entidad_territorial", uniqueConstraints = @UniqueConstraint(name="uk_entidad_territorial_nombre_pais", columnNames = {"nombre_entidad_territorial", "id_comuna"}))
public class EntidadTerritorial {
    @Id
    @Column(name="id_entidad_territorial")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entidad_territorial_seq")
    @SequenceGenerator(
            name = "entidad_territorial_seq",
            sequenceName = "SEC_ENTIDAD_TERRITORIAL", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long idEntidadTerritorial;

    @Column(name="nombre_entidad_territorial", length = 30, nullable = false)
    private String nombreEntidadTerritorial;

    @Column(name = "descripcion_entidad_territorial", length = 50)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comuna", foreignKey = @ForeignKey(name = "fk_entidad_territorial_pais"))
    private Pais pais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_entidad_territorial_usuario_registro"))
    private Usuario usuarioRegirstro;
}
