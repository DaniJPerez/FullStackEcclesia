package com.proyectoBase.gestionEcclesia.modele;

import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_contribuciones", uniqueConstraints = @UniqueConstraint(name = "uk_tipo_contribucion_nombre", columnNames = {"nombre_tipo_contribucion"}))
public  class TipoContribucion {
    @Id
    @Column(name = "id_tipo_contribucion")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_contribucion_seq")
    @SequenceGenerator(
            name = "tipo_contribucion_seq",
            sequenceName = "SEC_TIPO_CONTRIBUCION", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name = "nombre_tipo_contribucion", length = 30)
    private String nombre;

    @Column(name = "descripcion_tipo_contribucion", length = 50)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_tipo_contribucion_registro"))
    private Usuario usuarioRegirstro;
}
