package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contribuciones", uniqueConstraints = @UniqueConstraint(name = "uk_contribucion_miembro_evento_tipo_fecha", columnNames = {"id_miembro", "id_evento", "id_tipo_contribucion", "fecha_contribucion"}))
public class Contribucion {
    @Id
    @Column(name = "id_contribucion")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contribucion_seq")
    @SequenceGenerator(
            name = "contribucion_seq",
            sequenceName = "SEC_CONTRIBUCION", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name = "fecha_contribucion")
    private LocalDate fechaContribucion;

    @Column(name = "monto", precision = 10, scale = 2)
    @Positive
    private BigDecimal monto;

    @Column(name = "observaciones", length = 35)
    private String descripcion;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_miembro", foreignKey = @ForeignKey(name="FK_MIEMBRO_CONTRIBUCION"))
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_evento", foreignKey = @ForeignKey(name="FK_EVENTO_CONTRIBUCION"))
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_contribucion", foreignKey = @ForeignKey(name="FK_TIPO_CONTRIBUCION"))
    private TipoContribucion tipoContribucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_contribucion_usuario_registro"))
    private Usuario usuarioRegirstro;
}
