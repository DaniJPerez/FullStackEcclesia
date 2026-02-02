package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gastos", uniqueConstraints = @UniqueConstraint(name= "uk_gasto_fecha_registro", columnNames = {"fecha_registro"}))
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "gastos")
public class Gasto {
    @Id
    @Column(name = "id_gastos")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gasto_seq")
    @SequenceGenerator(
            name = "gasto_seq",
            sequenceName = "SEC_GASTO", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name = "monto")
    @Positive
    private BigDecimal monto;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name= "descripcion", length = 55)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "FK_GASTOS_USUARIO_REGISTRO"))
    private Usuario usuarioRegistro;

}
