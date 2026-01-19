package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asistencia_evento", uniqueConstraints = @UniqueConstraint(name = "uk_asistencia_evento_miembro_evento", columnNames = {"id_miembro", "id_evento"}))
public class AsistenciaEvento {
    @Id
    @Column(name = "id_asistencia_evento", precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asistencia_seq")
    @SequenceGenerator(
            name = "asistencia_seq",
            sequenceName = "SEC_ASISTENCIA", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long idAsistenciaEvento;

    @Column(name = "asistencia")
    @ColumnDefault("0")
    private Boolean asistencia = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_miembro", nullable = false)
    private Persona persona;

    @Column(name = "fecha_asistencia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaAsistencia = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_asistencia_usuario_registro"))
    private Usuario usuarioRegirstro;

}
