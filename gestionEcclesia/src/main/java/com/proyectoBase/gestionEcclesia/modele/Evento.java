package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Table(name = "eventos", uniqueConstraints = @UniqueConstraint(name = "uk_evento_nombre_fecha_inicio", columnNames = {"nombre_evento", "fecha_inicio"}))
public class Evento {
    @Id
    @Column(name = "id_evento")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evento_seq")
    @SequenceGenerator(
            name = "evento_seq",
            sequenceName = "SEC_ROL", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name="nombre_evento", length = 30, nullable = false)
    private String nombre;

    @Column(name="descripcion_evento", length =35 )
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_evento", foreignKey = @ForeignKey(name="FK_TIPO_EVENTO"))
    private TipoEvento tipoEvento;

    @Column(name = "fecha_inicio", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @Column(name = "tipo_restriccion_evento")
    @Enumerated(EnumType.STRING)
    private TipoAccesibilidadEvento accesibilidadEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion", foreignKey = @ForeignKey(name="FK_DIRECCION_EVENTO"))
    private Direccion direccion;

    @Column(name = "activo_evento")
    @ColumnDefault("1")
    private boolean activo;

    @OneToMany(mappedBy = "evento")
    private List<Contribucion> contribuciones = new ArrayList<>();

    @OneToMany(mappedBy = "evento")
    private List<AsistenciaEvento> asistentes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "FK_EVENTO_USUARIO_REGISTRO"))
    private Usuario usuarioRegistro;

    public void agregarContribucion(Contribucion nuevacontribucion){
        this.contribuciones.add(nuevacontribucion);
        nuevacontribucion.setEvento(this);
    }

    public void agregarAsistencia(AsistenciaEvento nuevaAsistenciaEvento){
        this.asistentes.add(nuevaAsistenciaEvento);
        nuevaAsistenciaEvento.setAsistencia(true);
        nuevaAsistenciaEvento.setEvento(this);
    }

}
