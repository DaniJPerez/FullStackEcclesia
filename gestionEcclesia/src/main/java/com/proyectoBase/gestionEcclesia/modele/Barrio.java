package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "barrios", uniqueConstraints = @UniqueConstraint(name="uk_barrio_nombre_comuna", columnNames = {"nombre_barrio", "id_comuna"}))
public class Barrio {
    @Id
    @Column(name = "id_barrio", precision = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "barrio_seq")
    @SequenceGenerator(
            name = "barrio_seq",
            sequenceName = "SEC_BARRIO", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name = "nombre_barrio", length = 35, nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comuna", foreignKey = @ForeignKey(name = "fk_barrio_comuna"))
    private Comuna comuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_barrio_usuario_registro"))
    private Usuario usuarioRegirstro;
}
