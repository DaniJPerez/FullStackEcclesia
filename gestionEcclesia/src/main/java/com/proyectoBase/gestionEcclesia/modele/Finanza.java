package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "finanzas", uniqueConstraints = @UniqueConstraint(name= "uk_finanza_fecha_actualizacion", columnNames = {"fecha_actualizacion"}))
public class Finanza {
    @Id
    @Column(name = "id_finanzas")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finanza_seq")
    @SequenceGenerator(
            name = "finanza_seq",
            sequenceName = "SEC_FINANZA", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name = "activo_total")
    private BigDecimal activoTotal;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_finanzas_usuario_registro"))
    private Usuario usuarioRegirstro;
}
