package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recursos", uniqueConstraints = @UniqueConstraint(name = "uk_recurso_descripcion_donante", columnNames = {"descripcion_recurso", "id_donante"}))
public abstract class Recurso {
    @Id
    @Column(name = "id_recurso")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recurso_seq")
    @SequenceGenerator(
            name = "recurso_seq",
            sequenceName = "SEC_RECURSO",
            allocationSize = 1
    )
    protected Long idRecurso;

    @Column(name= "descripcion_recurso", length = 50)
    protected String descripcionRecurso;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate fechaAdquisicion;

    @Column(name = "estado_recurso", length = 15)
    @ColumnDefault("'ACTIVO'")
    protected String estado;

    @ManyToOne
    @JoinColumn(name = "id_donante")
    protected Persona donante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_iglesia", foreignKey = @ForeignKey(name = "FK_RECURSO_IGLESIA"))
    private Iglesia iglesia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "FK_RECURSO_USUARIO_REGISTRO"))
    protected Usuario usuarioRegistro;
}
